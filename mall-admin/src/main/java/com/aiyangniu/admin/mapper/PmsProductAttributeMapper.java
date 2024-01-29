package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.bo.PmsProductAttrInfo;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性管理Mapper
 *
 * @author lzq
 * @date 2024/01/29
 */
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {

    /**
     * 获取商品分类对应属性列表
     *
     * @param productCategoryId 商品分类ID
     * @return 属性列表
     */
    List<PmsProductAttrInfo> getProductAttrInfo(@Param("id") Long productCategoryId);
}
