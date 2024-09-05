package com.aiyangniu.gate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aiyangniu.entity.model.bo.PmsGateProductDetail;
import com.aiyangniu.entity.model.bo.PmsProductCategoryNode;
import com.aiyangniu.entity.model.pojo.pms.*;
import com.aiyangniu.gate.mapper.*;
import com.aiyangniu.gate.service.PmsGateProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 前台商品管理实现类
 *
 * @author lzq
 * @date 2024/08/30
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsGateProductServiceImpl implements PmsGateProductService {

    private final PmsGateProductMapper pmsGateProductMapper;
    private final PmsProductCategoryMapper pmsProductCategoryMapper;
    private final PmsBrandMapper brandMapper;
    private final PmsProductAttributeMapper productAttributeMapper;
    private final PmsProductAttributeValueMapper productAttributeValueMapper;
    private final PmsSkuStockMapper skuStockMapper;
    private final PmsProductLadderMapper productLadderMapper;
    private final PmsProductFullReductionMapper productFullReductionMapper;
    private final GateProductMapper gateProductMapper;

    @Override
    public List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<PmsProduct> lqw = new LambdaQueryWrapper();
        lqw.like(StrUtil.isNotEmpty(keyword), PmsProduct::getName, keyword);
        lqw.eq(brandId != null, PmsProduct::getBrandId, brandId);
        lqw.eq(productCategoryId != null, PmsProduct::getProductCategoryId, productCategoryId);
        // 1->按新品；2->按销量；3->价格从低到高；4->价格从高到低
        switch (sort){
            case 1:
                lqw.orderByDesc(PmsProduct::getId);
                break;
            case 2:
                lqw.orderByDesc(PmsProduct::getSale);
                break;
            case 3:
                lqw.orderByAsc(PmsProduct::getPrice);
                break;
            case 4:
                lqw.orderByDesc(PmsProduct::getPrice);
                break;
            default:
                break;
        }
        return pmsGateProductMapper.selectList(lqw);
    }

    @Override
    public List<PmsProductCategoryNode> categoryTreeList() {
        List<PmsProductCategory> list = pmsProductCategoryMapper.selectList(new LambdaQueryWrapper<>());
        return list.stream()
                .filter(item -> item.getParentId().equals(0L))
                .map(item -> covert(item, list))
                .collect(Collectors.toList());
    }

    @Override
    public PmsGateProductDetail detail(Long id) {
        PmsGateProductDetail result = new PmsGateProductDetail();
        // 获取商品信息
        PmsProduct product = pmsGateProductMapper.selectById(id);
        result.setProduct(product);
        // 获取品牌信息
        PmsBrand brand = brandMapper.selectById(product.getBrandId());
        result.setBrand(brand);
        // 获取商品属性信息
        List<PmsProductAttribute> productAttributeList = productAttributeMapper.selectList(new LambdaQueryWrapper<PmsProductAttribute>().eq(PmsProductAttribute::getProductAttributeCategoryId, product.getProductAttributeCategoryId()));
        result.setProductAttributeList(productAttributeList);
        // 获取商品属性值信息
        if(CollUtil.isNotEmpty(productAttributeList)){
            List<Long> attributeIds = productAttributeList.stream().map(PmsProductAttribute::getId).collect(Collectors.toList());
            List<PmsProductAttributeValue> productAttributeValueList = productAttributeValueMapper.selectList(new LambdaQueryWrapper<PmsProductAttributeValue>().eq(PmsProductAttributeValue::getProductId, product.getId()).in(PmsProductAttributeValue::getProductAttributeId, attributeIds));
            result.setProductAttributeValueList(productAttributeValueList);
        }
        // 获取商品SKU库存信息
        List<PmsSkuStock> skuStockList = skuStockMapper.selectList(new LambdaQueryWrapper<PmsSkuStock>().eq(PmsSkuStock::getProductId, product.getId()));
        result.setSkuStockList(skuStockList);
        // 商品阶梯价格设置
        if(product.getPromotionType() == 3){
            List<PmsProductLadder> productLadderList = productLadderMapper.selectList(new LambdaQueryWrapper<PmsProductLadder>().eq(PmsProductLadder::getProductId, product.getId()));
            result.setProductLadderList(productLadderList);
        }
        // 商品满减价格设置
        if(product.getPromotionType() == 4){
            List<PmsProductFullReduction> productFullReductionList = productFullReductionMapper.selectList(new LambdaQueryWrapper<PmsProductFullReduction>().eq(PmsProductFullReduction::getProductId, product.getId()));
            result.setProductFullReductionList(productFullReductionList);
        }
        // 商品可用优惠券
        result.setCouponList(gateProductMapper.getAvailableCouponList(product.getId(), product.getProductCategoryId()));
        return result;
    }

    /**
     * 初始对象转化为节点对象
     */
    private PmsProductCategoryNode covert(PmsProductCategory item, List<PmsProductCategory> list) {
        PmsProductCategoryNode node = new PmsProductCategoryNode();
        BeanUtils.copyProperties(item, node);
        List<PmsProductCategoryNode> children = list.stream()
                .filter(subItem -> subItem.getParentId().equals(item.getId()))
                .map(subItem -> covert(subItem, list))
                .collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
