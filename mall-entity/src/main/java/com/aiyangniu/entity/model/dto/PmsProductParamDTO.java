package com.aiyangniu.entity.model.dto;

import com.aiyangniu.entity.model.pojo.cms.CmsPreferenceAreaProductRelation;
import com.aiyangniu.entity.model.pojo.cms.CmsSubjectProductRelation;
import com.aiyangniu.entity.model.pojo.pms.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 创建和修改商品的请求参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@EqualsAndHashCode
public class PmsProductParamDTO extends PmsProduct {

    private static final long serialVersionUID = -846449818156508178L;

    @ApiModelProperty("商品阶梯价格设置")
    private List<PmsProductLadder> productLadderList;

    @ApiModelProperty("商品满减价格设置")
    private List<PmsProductFullReduction> productFullReductionList;

    @ApiModelProperty("商品会员价格设置")
    private List<PmsMemberPrice> memberPriceList;

    @ApiModelProperty("商品的sku库存信息")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("商品参数及自定义规格属性")
    private List<PmsProductAttributeValue> productAttributeValueList;

    @ApiModelProperty("专题和商品关系")
    private List<CmsSubjectProductRelation> subjectProductRelationList;

    @ApiModelProperty("优选专区和商品的关系")
    private List<CmsPreferenceAreaProductRelation> preferenceAreaProductRelationList;
}
