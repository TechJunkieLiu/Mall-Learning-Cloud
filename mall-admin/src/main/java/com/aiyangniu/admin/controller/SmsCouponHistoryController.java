package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsCouponHistoryService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 优惠券领取记录管理
 *
 * @author lzq
 * @date 2024/02/28
 */
@Api(value = "SmsCouponHistoryController", tags = "优惠券领取记录管理")
@RestController
@RequestMapping("/couponHistory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsCouponHistoryController {

    private final SmsCouponHistoryService smsCouponHistoryService;

    @ApiOperation("根据优惠券id，使用状态，订单编号分页获取领取记录")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsCouponHistory>> list(
            @RequestParam(value = "couponId", required = false) Long couponId,
            @RequestParam(value = "useStatus", required = false) Integer useStatus,
            @RequestParam(value = "orderSn", required = false) String orderSn,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsCouponHistory> historyList = smsCouponHistoryService.list(couponId, useStatus, orderSn, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(historyList));
    }
}
