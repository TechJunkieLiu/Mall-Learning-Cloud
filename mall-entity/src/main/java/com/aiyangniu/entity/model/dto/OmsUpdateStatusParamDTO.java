package com.aiyangniu.entity.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 确认收货请求参数
 *
 * @author lzq
 * @date 2023/09/20
 */
@Getter
@Setter
public class OmsUpdateStatusParamDTO {

    @ApiModelProperty("服务单号")
    private Long id;

    @ApiModelProperty("收货地址关联id")
    private Long companyAddressId;

    @ApiModelProperty("确认退款金额")
    private BigDecimal returnAmount;

    @ApiModelProperty("处理备注")
    private String handleNote;

    @ApiModelProperty("处理人")
    private String handleMan;

    @ApiModelProperty("收货备注")
    private String receiveNote;

    @ApiModelProperty("收货人")
    private String receiveMan;

    @ApiModelProperty("申请状态：1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    @Override
    public String toString() {
        return "OmsUpdateStatusParamDTO{" +
                "id=" + id +
                ", companyAddressId=" + companyAddressId +
                ", returnAmount=" + returnAmount +
                ", handleNote='" + handleNote + '\'' +
                ", handleMan='" + handleMan + '\'' +
                ", receiveNote='" + receiveNote + '\'' +
                ", receiveMan='" + receiveMan + '\'' +
                ", status=" + status +
                '}';
    }
}
