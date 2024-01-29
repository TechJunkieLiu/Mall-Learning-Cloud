package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.PmsProductCategoryAttributeRelationMapper;
import com.aiyangniu.admin.mapper.PmsProductCategoryMapper;
import com.aiyangniu.admin.mapper.PmsProductMapper;
import com.aiyangniu.admin.service.PmsProductCategoryService;
import com.aiyangniu.entity.model.bo.PmsProductCategoryWithChildrenItem;
import com.aiyangniu.entity.model.dto.PmsProductCategoryParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategoryAttributeRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理实现类
 *
 * @author lzq
 * @date 2024/01/29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    private final PmsProductMapper pmsProductMapper;
    private final PmsProductCategoryMapper pmsProductCategoryMapper;
    private final PmsProductCategoryAttributeRelationMapper pmsProductCategoryAttributeRelationMapper;

    @Override
    public int create(PmsProductCategoryParamDTO dto) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(dto, productCategory);
        // 没有父分类时为一级分类
        setCategoryLevel(productCategory);
        int count = pmsProductCategoryMapper.insert(productCategory);
        // 创建筛选属性关联
        List<Long> productAttributeIdList = dto.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return count;
    }

    /**
     * 批量插入商品分类与筛选属性关系表
     *
     * @param productCategoryId 商品分类id
     * @param productAttributeIdList 相关商品筛选属性id集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        pmsProductCategoryAttributeRelationMapper.insertList(relationList);
    }

    @Override
    public int update(Long id, PmsProductCategoryParamDTO dto) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(dto, productCategory);
        setCategoryLevel(productCategory);
        // 更新商品分类时要更新商品中的名称
        PmsProduct product = new PmsProduct();
        product.setProductCategoryName(productCategory.getName());
        pmsProductMapper.update(product, new LambdaQueryWrapper<PmsProduct>().eq(PmsProduct::getProductCategoryId, id));

        // 同时更新筛选属性的信息
        if(!CollectionUtils.isEmpty(dto.getProductAttributeIdList())){
            pmsProductCategoryAttributeRelationMapper.delete(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>().eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id));
            insertRelationList(id, dto.getProductAttributeIdList());
        }else{
            pmsProductCategoryAttributeRelationMapper.delete(new LambdaQueryWrapper<PmsProductCategoryAttributeRelation>().eq(PmsProductCategoryAttributeRelation::getProductCategoryId, id));
        }
        return pmsProductCategoryMapper.updateById(productCategory);
    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return pmsProductCategoryMapper.selectList(new LambdaQueryWrapper<PmsProductCategory>().eq(PmsProductCategory::getParentId, parentId).orderByDesc(PmsProductCategory::getSort));
    }

    @Override
    public int delete(Long id) {
        return pmsProductCategoryMapper.deleteById(id);
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return pmsProductCategoryMapper.selectById(id);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setNavStatus(navStatus);
        return pmsProductCategoryMapper.update(productCategory, new LambdaQueryWrapper<PmsProductCategory>().in(PmsProductCategory::getId, ids));
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setShowStatus(showStatus);
        return pmsProductCategoryMapper.update(productCategory, new LambdaQueryWrapper<PmsProductCategory>().in(PmsProductCategory::getId, ids));
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return pmsProductCategoryMapper.listWithChildren();
    }

    /**
     * 根据分类的parentId设置分类的level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        // 没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            // 有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory = pmsProductCategoryMapper.selectById(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }
}
