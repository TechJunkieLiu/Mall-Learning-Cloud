package com.aiyangniu.entity.model.pojo.pms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品满减实体（只针对同商品）
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "pms_product_full_reduction", description = "商品满减表实体")
public class PmsProductFullReduction implements Serializable {

    private static final long serialVersionUID = -672123508745158900L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "满足价格")
    private BigDecimal fullPrice;

    @ApiModelProperty(value = "立减价格")
    private BigDecimal reducePrice;
}