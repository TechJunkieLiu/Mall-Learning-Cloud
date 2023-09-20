package com.aiyangniu.entity.model.pojo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 积分消费设置实体
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@ApiModel(value = "ums_integration_consume_setting", description = "积分消费设置表实体")
public class UmsIntegrationConsumeSetting implements Serializable {

    private static final long serialVersionUID = -6725296798090295791L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "每一元需要抵扣的积分数量")
    private Integer deductionPerAmount;

    @ApiModelProperty(value = "每笔订单最高抵用百分比")
    private Integer maxPercentPerOrder;

    @ApiModelProperty(value = "每次使用积分最小单位100")
    private Integer useUnit;

    @ApiModelProperty(value = "是否可以和优惠券同用；0->不可以；1->可以")
    private Integer couponStatus;
}