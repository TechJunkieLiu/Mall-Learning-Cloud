package com.aiyangniu.entity.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 修改订单费用信息参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class OmsMoneyInfoParamDTO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("运费金额")
    private BigDecimal freightAmount;

    @ApiModelProperty("管理员后台调整订单所使用的折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单")
    private Integer status;

    @Override
    public String toString() {
        return "OmsMoneyInfoDTO{" +
                "orderId=" + orderId +
                ", freightAmount=" + freightAmount +
                ", discountAmount=" + discountAmount +
                ", status=" + status +
                '}';
    }
}
