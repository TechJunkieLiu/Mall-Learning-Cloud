package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 购物车中带规格和SKU的商品信息
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class CartProduct extends PmsProduct {

    @ApiModelProperty("商品属性列表")
    private List<PmsProductAttribute> productAttributeList;

    @ApiModelProperty("商品SKU库存列表")
    private List<PmsSkuStock> skuStockList;
}
