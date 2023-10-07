package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.gate.service.OmsGateOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 前台订单管理
 *
 * @author lzq
 * @date 2023/10/07
 */
@Api(value = "OmsGateOrderController", tags = "前台订单管理")
@RestController
@RequestMapping("/gateOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderController {

    private final OmsGateOrderService omsGateOrderService;

    @ApiOperation("取消单个超时订单")
    @PostMapping(value = "/cancelOrder")
    public CommonResult cancelOrder(Long orderId) {
        omsGateOrderService.sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null);
    }
}
