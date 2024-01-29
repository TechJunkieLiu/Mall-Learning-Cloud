package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.PmsProductAttributeCategoryMapper;
import com.aiyangniu.admin.service.PmsProductAttributeCategoryService;
import com.aiyangniu.entity.model.bo.PmsProductAttributeCategoryItem;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品属性分类管理实现类
 *
 * @author lzq
 * @date 2024/01/29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    private final PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return productAttributeCategoryMapper.insert(productAttributeCategory);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return productAttributeCategoryMapper.updateById(productAttributeCategory);
    }

    @Override
    public int delete(Long id) {
        return productAttributeCategoryMapper.deleteById(id);
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryMapper.selectById(id);
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return productAttributeCategoryMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryMapper.getListWithAttr();
    }
}
