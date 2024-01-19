package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.OmsOrderSettingService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单设置管理
 *
 * @author lzq
 * @date 2024/01/19
 */
@Api(value = "OmsOrderSettingController", tags = "订单设置管理")
@RestController
@RequestMapping("/orderSetting")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsOrderSettingController {

    private final OmsOrderSettingService omsOrderSettingService;

    @ApiOperation("获取指定订单设置")
    @GetMapping(value = "/{id}")
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = omsOrderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @ApiOperation("修改指定订单设置")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = omsOrderSettingService.update(id, orderSetting);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
