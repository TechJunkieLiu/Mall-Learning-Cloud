package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.OmsOrderReturnApplyParam;
import com.aiyangniu.gate.service.OmsGateOrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台订单退货管理
 *
 * @author lzq
 * @date 2024/08/29
 */
@Api(value = "OmsGateOrderReturnApplyController", tags = "前台订单退货管理")
@RestController
@RequestMapping("/returnApply")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderReturnApplyController {

    private final OmsGateOrderReturnApplyService omsGateOrderReturnApplyService;

    @ApiOperation("申请退货")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody OmsOrderReturnApplyParam returnApply) {
        int count = omsGateOrderReturnApplyService.create(returnApply);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
