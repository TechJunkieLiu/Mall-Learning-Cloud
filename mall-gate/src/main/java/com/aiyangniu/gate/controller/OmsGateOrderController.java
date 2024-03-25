package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.ConfirmOrderResult;
import com.aiyangniu.entity.model.bo.OrderParam;
import com.aiyangniu.gate.service.OmsGateOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 前台订单管理
 *
 * @author lzq
 * @date 2024/03/22
 */
@Api(value = "OmsGateOrderController", tags = "前台订单管理")
@RestController
@RequestMapping("/gateOrder")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderController {

    private final OmsGateOrderService omsGateOrderService;

    @ApiOperation("根据购物车信息生成确认单")
    @PostMapping(value = "/generateConfirmOrder")
    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestBody List<Long> cartIds) {
        ConfirmOrderResult confirmOrderResult = omsGateOrderService.generateConfirmOrder(cartIds);
        return CommonResult.success(confirmOrderResult);
    }

    @ApiOperation("根据购物车信息生成订单")
    @PostMapping(value = "/generateOrder")
    public CommonResult generateOrder(@RequestBody OrderParam orderParam) {
        Map<String, Object> result = omsGateOrderService.generateOrder(orderParam);
        return CommonResult.success(result, "下单成功");
    }

    @ApiOperation("取消单个超时订单")
    @PostMapping(value = "/cancelOrder")
    public CommonResult cancelOrder(Long orderId) {
        omsGateOrderService.sendDelayMessageCancelOrder(orderId);
        return CommonResult.success(null);
    }
}
