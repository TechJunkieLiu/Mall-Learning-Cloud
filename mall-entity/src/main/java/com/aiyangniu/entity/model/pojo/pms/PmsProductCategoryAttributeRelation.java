package com.aiyangniu.entity.model.pojo.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品分类和属性关系实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "pms_product_category_attribute_relation", description = "商品分类和属性关系表实体")
public class PmsProductCategoryAttributeRelation implements Serializable {

    private static final long serialVersionUID = 4312518717632552482L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品分类ID")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品属性ID")
    private Long productAttributeId;
}