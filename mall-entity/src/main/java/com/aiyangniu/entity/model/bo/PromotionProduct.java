package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductFullReduction;
import com.aiyangniu.entity.model.pojo.pms.PmsProductLadder;
import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 促销商品信息，包括sku、打折优惠、满减优惠
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class PromotionProduct extends PmsProduct {

    private static final long serialVersionUID = 202263160914197752L;

    @ApiModelProperty("商品库存信息")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("商品打折信息")
    private List<PmsProductLadder> productLadderList;

    @ApiModelProperty("商品满减信息")
    private List<PmsProductFullReduction> productFullReductionList;
}
