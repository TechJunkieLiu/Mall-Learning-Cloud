package com.aiyangniu.entity.model.pojo.sms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 限时购和商品关系实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "sms_flash_promotion_product_relation", description = "限时购和商品关系表实体")
public class SmsFlashPromotionProductRelation implements Serializable {

    private static final long serialVersionUID = 2149166492595184847L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "限时购ID")
    private Long flashPromotionId;

    @ApiModelProperty(value = "限时购场次ID")
    private Long flashPromotionSessionId;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "限时购价格")
    private BigDecimal flashPromotionPrice;

    @ApiModelProperty(value = "限时购数量")
    private Integer flashPromotionCount;

    @ApiModelProperty(value = "每人限购数量")
    private Integer flashPromotionLimit;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}