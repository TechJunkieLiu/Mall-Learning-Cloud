package com.aiyangniu.entity.model.bo;

import com.aiyangniu.entity.model.pojo.oms.OmsCompanyAddress;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderReturnApply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 申请信息封装
 *
 * @author lzq
 * @date 2023/09/20
 */
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {

    private static final long serialVersionUID = -7387977404916836014L;

    @Getter
    @Setter
    @ApiModelProperty(value = "公司收货地址")
    private OmsCompanyAddress companyAddress;

    @Override
    public String toString() {
        return "OmsOrderReturnApplyResultDTO{" +
                "companyAddress=" + companyAddress +
                '}';
    }
}
