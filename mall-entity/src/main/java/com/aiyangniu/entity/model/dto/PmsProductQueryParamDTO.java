package com.aiyangniu.entity.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品查询参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductQueryParamDTO {

    @ApiModelProperty("上架状态")
    private Integer publishStatus;

    @ApiModelProperty("审核状态")
    private Integer verifyStatus;

    @ApiModelProperty("商品名称模糊关键字")
    private String keyword;

    @ApiModelProperty("商品货号")
    private String productSn;

    @ApiModelProperty("商品分类编号")
    private Long productCategoryId;

    @ApiModelProperty("商品品牌编号")
    private Long brandId;
}
