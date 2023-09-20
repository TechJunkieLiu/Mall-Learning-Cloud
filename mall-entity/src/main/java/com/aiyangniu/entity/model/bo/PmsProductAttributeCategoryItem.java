package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttributeCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 带有属性的商品属性分类
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {

    private static final long serialVersionUID = -8189850916110593516L;

    @ApiModelProperty(value = "商品属性列表")
    private List<PmsProductAttribute> productAttributeList;
}
