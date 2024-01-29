package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.PmsProductAttributeCategoryMapper;
import com.aiyangniu.admin.mapper.PmsProductAttributeMapper;
import com.aiyangniu.admin.service.PmsProductAttributeService;
import com.aiyangniu.entity.model.bo.PmsProductAttrInfo;
import com.aiyangniu.entity.model.dto.PmsProductAttributeParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品属性管理实现类
 *
 * @author lzq
 * @date 2024/01/29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    private final PmsProductAttributeMapper productAttributeMapper;
    private final PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return productAttributeMapper.selectList(new LambdaQueryWrapper<PmsProductAttribute>().eq(PmsProductAttribute::getProductAttributeCategoryId, cid).eq(PmsProductAttribute::getType, type).orderByDesc(PmsProductAttribute::getSort));
    }

    @Override
    public int create(PmsProductAttributeParamDTO dto) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(dto, pmsProductAttribute);
        int count = productAttributeMapper.insert(pmsProductAttribute);
        // 新增商品属性以后需要更新商品属性分类数量
        PmsProductAttributeCategory pmsProductAttributeCategory = productAttributeCategoryMapper.selectById(pmsProductAttribute.getProductAttributeCategoryId());
        if(pmsProductAttribute.getType() == 0){
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() + 1);
        }else if(pmsProductAttribute.getType() == 1){
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() + 1);
        }
        productAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public int update(Long id, PmsProductAttributeParamDTO dto) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        pmsProductAttribute.setId(id);
        BeanUtils.copyProperties(dto, pmsProductAttribute);
        return productAttributeMapper.updateById(pmsProductAttribute);
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeMapper.selectById(id);
    }

    @Override
    public int delete(List<Long> ids) {
        // 获取分类
        PmsProductAttribute pmsProductAttribute = productAttributeMapper.selectById(ids.get(0));
        Integer type = pmsProductAttribute.getType();
        PmsProductAttributeCategory pmsProductAttributeCategory = productAttributeCategoryMapper.selectById(pmsProductAttribute.getProductAttributeCategoryId());
        int count = productAttributeMapper.delete(new LambdaQueryWrapper<PmsProductAttribute>().in(PmsProductAttribute::getId, ids));
        // 删除完成后修改数量
        if(type == 0){
            if(pmsProductAttributeCategory.getAttributeCount() >= count){
                pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() - count);
            }else{
                pmsProductAttributeCategory.setAttributeCount(0);
            }
        }else if(type==1){
            if(pmsProductAttributeCategory.getParamCount() >= count){
                pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() - count);
            }else{
                pmsProductAttributeCategory.setParamCount(0);
            }
        }
        productAttributeCategoryMapper.updateById(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public List<PmsProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return productAttributeMapper.getProductAttrInfo(productCategoryId);
    }
}
