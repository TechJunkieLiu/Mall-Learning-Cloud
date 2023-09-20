package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.bo.PmsProductAttributeCategoryItem;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 商品属性分类管理Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface PmsProductAttributeCategoryMapper extends BaseMapper<PmsProductAttributeCategory> {

    /**
     * 获取包含属性的商品属性分类
     *
     * @return 商品分类列表（包含属性）
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
