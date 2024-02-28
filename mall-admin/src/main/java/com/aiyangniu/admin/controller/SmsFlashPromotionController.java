package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsFlashPromotionService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购活动管理
 *
 * @author lzq
 * @date 2024/02/28
 */
@Api(value = "SmsFlashPromotionController", tags = "限时购活动管理")
@RestController
@RequestMapping("/flash")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsFlashPromotionController {

    private final SmsFlashPromotionService smsFlashPromotionService;

    @ApiOperation("添加活动")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody SmsFlashPromotion flashPromotion) {
        int count = smsFlashPromotionService.create(flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("编辑活动")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsFlashPromotion flashPromotion) {
        int count = smsFlashPromotionService.update(id, flashPromotion);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除活动")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = smsFlashPromotionService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改上下线状态")
    @PostMapping(value = "/update/status/{id}")
    public CommonResult update(@PathVariable Long id, Integer status) {
        int count = smsFlashPromotionService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取活动详情")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsFlashPromotion> getItem(@PathVariable Long id) {
        SmsFlashPromotion flashPromotion = smsFlashPromotionService.getItem(id);
        return CommonResult.success(flashPromotion);
    }

    @ApiOperation("根据活动名称分页查询")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsFlashPromotion>> getItem(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsFlashPromotion> flashPromotionList = smsFlashPromotionService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(flashPromotionList));
    }
}
