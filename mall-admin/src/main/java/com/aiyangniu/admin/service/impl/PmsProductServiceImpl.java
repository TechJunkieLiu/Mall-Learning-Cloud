package com.aiyangniu.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.*;
import com.aiyangniu.admin.service.PmsProductService;
import com.aiyangniu.entity.model.bo.PmsProductResult;
import com.aiyangniu.entity.model.dto.PmsProductParamDTO;
import com.aiyangniu.entity.model.dto.PmsProductQueryParamDTO;
import com.aiyangniu.entity.model.pojo.cms.CmsPreferenceAreaProductRelation;
import com.aiyangniu.entity.model.pojo.cms.CmsSubjectProductRelation;
import com.aiyangniu.entity.model.pojo.pms.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品管理实现类
 *
 * @author lzq
 * @date 2024/01/26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductServiceImpl implements PmsProductService {

    private final PmsProductMapper pmsProductMapper;
    private final PmsMemberPriceMapper pmsMemberPriceMapper;
    private final PmsProductLadderMapper pmsProductLadderMapper;
    private final PmsProductFullReductionMapper pmsProductFullReductionMapper;
    private final PmsSkuStockMapper pmsSkuStockMapper;
    private final PmsProductAttributeValueMapper pmsProductAttributeValueMapper;
    private final CmsSubjectProductRelationMapper cmsSubjectProductRelationMapper;
    private final CmsPreferenceAreaProductRelationMapper cmsPreferenceAreaProductRelationMapper;
    private final PmsProductVerifyRecordMapper pmsProductVerifyRecordMapper;

    @Override
    public int create(PmsProductParamDTO dto) {
        int count = 1;
        // 创建商品
        PmsProduct product = new PmsProduct();
        BeanUtil.copyProperties(dto, product);
        product.setId(null);
        pmsProductMapper.insert(product);
        // 根据促销类型设置价格：会员价格、阶梯价格、满减价格
        Long productId = product.getId();
        // 会员价格
        relateAndInsertList(pmsMemberPriceMapper, dto.getMemberPriceList(), productId);
        // 阶梯价格
        relateAndInsertList(pmsProductLadderMapper, dto.getProductLadderList(), productId);
        // 满减价格
        relateAndInsertList(pmsProductFullReductionMapper, dto.getProductFullReductionList(), productId);
        // 处理sku的编码
        handleSkuStockCode(dto.getSkuStockList(), productId);
        // 添加sku库存信息
        relateAndInsertList(pmsSkuStockMapper, dto.getSkuStockList(), productId);
        // 添加商品参数,添加自定义商品规格
        relateAndInsertList(pmsProductAttributeValueMapper, dto.getProductAttributeValueList(), productId);
        // 关联专题
        relateAndInsertList(cmsSubjectProductRelationMapper, dto.getSubjectProductRelationList(), productId);
        // 关联优选
        relateAndInsertList(cmsPreferenceAreaProductRelationMapper, dto.getPreferenceAreaProductRelationList(), productId);
        return count;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StrUtil.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                // 日期
                sb.append(sdf.format(new Date()));
                // 四位商品id
                sb.append(String.format("%04d", productId));
                // 三位索引id
                sb.append(String.format("%03d", i+1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return pmsProductMapper.getUpdateInfo(id);
    }

    @Override
    public int update(Long id, PmsProductParamDTO productParam) {
        int count;
        // 更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        pmsProductMapper.updateById(product);
        // 会员价格
        pmsMemberPriceMapper.delete(new LambdaQueryWrapper<PmsMemberPrice>().eq(PmsMemberPrice::getProductId, id));
        relateAndInsertList(pmsMemberPriceMapper, productParam.getMemberPriceList(), id);
        // 阶梯价格
        pmsProductLadderMapper.delete(new LambdaQueryWrapper<PmsProductLadder>().eq(PmsProductLadder::getProductId, id));
        relateAndInsertList(pmsProductLadderMapper, productParam.getProductLadderList(), id);
        // 满减价格
        pmsProductFullReductionMapper.delete(new LambdaQueryWrapper<PmsProductFullReduction>().eq(PmsProductFullReduction::getProductId, id));
        relateAndInsertList(pmsProductFullReductionMapper, productParam.getProductFullReductionList(), id);
        // 修改sku库存信息
        handleUpdateSkuStockList(id, productParam);
        // 修改商品参数，添加自定义商品规格
        pmsProductAttributeValueMapper.delete(new LambdaQueryWrapper<PmsProductAttributeValue>().eq(PmsProductAttributeValue::getProductId, id));
        relateAndInsertList(pmsProductAttributeValueMapper, productParam.getProductAttributeValueList(), id);
        // 关联专题
        cmsSubjectProductRelationMapper.delete(new LambdaQueryWrapper<CmsSubjectProductRelation>().eq(CmsSubjectProductRelation::getProductId, id));
        relateAndInsertList(cmsSubjectProductRelationMapper, productParam.getSubjectProductRelationList(), id);
        // 关联优选
        cmsPreferenceAreaProductRelationMapper.delete(new LambdaQueryWrapper<CmsPreferenceAreaProductRelation>().eq(CmsPreferenceAreaProductRelation::getProductId, id));
        relateAndInsertList(cmsPreferenceAreaProductRelationMapper, productParam.getPreferenceAreaProductRelationList(), id);

        count = 1;
        return count;
    }

    private void handleUpdateSkuStockList(Long id, PmsProductParamDTO productParam) {
        // 当前的sku信息
        List<PmsSkuStock> currSkuList = productParam.getSkuStockList();
        // 当前没有sku直接删除
        if(CollUtil.isEmpty(currSkuList)){
            pmsSkuStockMapper.delete(new LambdaQueryWrapper<PmsSkuStock>().eq(PmsSkuStock::getProductId, id));
            return;
        }
        // 获取初始sku信息
        List<PmsSkuStock> oriStuList = pmsSkuStockMapper.selectList(new LambdaQueryWrapper<PmsSkuStock>().eq(PmsSkuStock::getProductId, id));
        // 获取新增sku信息
        List<PmsSkuStock> insertSkuList = currSkuList.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        // 获取需要更新的sku信息
        List<PmsSkuStock> updateSkuList = currSkuList.stream().filter(item -> item.getId() != null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        // 获取需要删除的sku信息
        List<PmsSkuStock> removeSkuList = oriStuList.stream().filter(item -> !updateSkuIds.contains(item.getId())).collect(Collectors.toList());
        handleSkuStockCode(insertSkuList, id);
        handleSkuStockCode(updateSkuList, id);
        // 新增sku
        if(CollUtil.isNotEmpty(insertSkuList)){
            relateAndInsertList(pmsSkuStockMapper, insertSkuList, id);
        }
        // 删除sku
        if(CollUtil.isNotEmpty(removeSkuList)){
            List<Long> removeSkuIds = removeSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
            pmsSkuStockMapper.delete(new LambdaQueryWrapper<PmsSkuStock>().in(PmsSkuStock::getId, removeSkuIds));
        }
        // 修改sku
        if(CollUtil.isNotEmpty(updateSkuList)){
            for (PmsSkuStock pmsSkuStock : updateSkuList) {
                pmsSkuStockMapper.updateById(pmsSkuStock);
            }
        }
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParamDTO dto, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductMapper.selectList(new LambdaQueryWrapper<PmsProduct>()
                .eq(PmsProduct::getDeleteStatus, 0)
                .eq(dto.getPublishStatus() != null, PmsProduct::getPublishStatus, dto.getPublishStatus())
                .eq(dto.getVerifyStatus() != null, PmsProduct::getVerifyStatus, dto.getVerifyStatus())
                .like(!StrUtil.isEmpty(dto.getKeyword()), PmsProduct::getName, dto.getKeyword())
                .like(!StrUtil.isEmpty(dto.getProductSn()), PmsProduct::getProductSn, dto.getProductSn())
                .eq(dto.getBrandId() != null, PmsProduct::getBrandId, dto.getBrandId())
                .eq(dto.getProductCategoryId() != null, PmsProduct::getProductCategoryId, dto.getProductCategoryId())
        );
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        int count = pmsProductMapper.update(product, new LambdaQueryWrapper<PmsProduct>().in(PmsProduct::getId, ids));
        List<PmsProductVerifyRecord> list = new ArrayList<>();
        // 修改完审核状态后插入审核记录
        for (Long id : ids) {
            PmsProductVerifyRecord record = new PmsProductVerifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVerifyMan("test");
            list.add(record);
        }
        pmsProductVerifyRecordMapper.insertList(list);
        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);
        return pmsProductMapper.update(record, new LambdaQueryWrapper<PmsProduct>().in(PmsProduct::getId, ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommendStatus(recommendStatus);
        return pmsProductMapper.update(record, new LambdaQueryWrapper<PmsProduct>().in(PmsProduct::getId, ids));
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);
        return pmsProductMapper.update(record, new LambdaQueryWrapper<PmsProduct>().in(PmsProduct::getId, ids));
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        return pmsProductMapper.update(record, new LambdaQueryWrapper<PmsProduct>().in(PmsProduct::getId, ids));
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        return pmsProductMapper.selectList(new LambdaQueryWrapper<PmsProduct>()
                .eq(PmsProduct::getDeleteStatus, 0)
                .like(!StrUtil.isEmpty(keyword), PmsProduct::getName, keyword)
                .or()
                .eq(PmsProduct::getDeleteStatus, 0)
                .like(!StrUtil.isEmpty(keyword), PmsProduct::getProductSn, keyword)
        );
    }

    /**
     * 建立和插入关系表操作
     *
     * @param mapper    可以操作的mapper
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object mapper, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = mapper.getClass().getMethod("insertList", List.class);
            insertList.invoke(mapper, dataList);
        } catch (Exception e) {
            log.warn("创建产品出错: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
