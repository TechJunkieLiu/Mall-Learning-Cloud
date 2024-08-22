package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.ConfirmOrderResult;
import com.aiyangniu.entity.model.bo.OmsOrderDetail;
import com.aiyangniu.entity.model.bo.OrderParam;
import com.aiyangniu.gate.service.OmsGateOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

    @ApiOperation("用户支付成功的回调")
    @PostMapping(value = "/paySuccess")
    public CommonResult paySuccess(@RequestParam Long orderId, @RequestParam Integer payType) {
        Integer count = omsGateOrderService.paySuccess(orderId, payType);
        return CommonResult.success(count, "支付成功");
    }

    @ApiOperation("自动取消超时订单")
    @PostMapping(value = "/cancelTimeOutOrder")
    public CommonResult cancelTimeOutOrder() {
        omsGateOrderService.cancelTimeOutOrder();
        return CommonResult.success(null);
    }

    @ApiOperation("按状态分页获取用户订单列表")
    @ApiImplicitParam(name = "status", value = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭", defaultValue = "-1", allowableValues = "-1,0,1,2,3,4", paramType = "query", dataType = "int")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<OmsOrderDetail>> list(
            @RequestParam Integer status,
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ) {
        CommonPage<OmsOrderDetail> orderPage = omsGateOrderService.list(status, pageNum, pageSize);
        return CommonResult.success(orderPage);
    }


}
