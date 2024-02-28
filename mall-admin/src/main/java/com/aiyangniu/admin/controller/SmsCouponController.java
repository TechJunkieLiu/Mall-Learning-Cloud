package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsCouponService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.dto.SmsCouponParamDTO;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 优惠券管理
 *
 * @author lzq
 * @date 2024/02/28
 */
@Api(value = "SmsCouponController", tags = "优惠券管理")
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsCouponController {

    private final SmsCouponService smsCouponService;

    @ApiOperation("添加优惠券")
    @PostMapping(value = "/create")
    public CommonResult add(@RequestBody SmsCouponParamDTO couponParam) {
        int count = smsCouponService.create(couponParam);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除优惠券")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = smsCouponService.delete(id);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改优惠券")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsCouponParamDTO couponParam) {
        int count = smsCouponService.update(id, couponParam);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据优惠券名称和类型分页获取优惠券列表")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsCoupon>> list(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "type",required = false) Integer type,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsCoupon> couponList = smsCouponService.list(name, type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(couponList));
    }

    @ApiOperation("获取单个优惠券的详细信息")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsCouponParamDTO> getItem(@PathVariable Long id) {
        SmsCouponParamDTO couponParam = smsCouponService.getItem(id);
        return CommonResult.success(couponParam);
    }
}
