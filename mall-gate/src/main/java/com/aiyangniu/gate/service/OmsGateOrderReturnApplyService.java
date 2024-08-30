package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.OmsOrderReturnApplyParam;

/**
 * 前台订单退货管理接口
 *
 * @author lzq
 * @date 2024/08/29
 */
public interface OmsGateOrderReturnApplyService {

    /**
     * 提交申请
     *
     * @param returnApply 退货申请请求参数
     * @return 提交个数
     */
    int create(OmsOrderReturnApplyParam returnApply);
}
