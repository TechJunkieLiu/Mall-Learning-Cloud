package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsFlashPromotionSessionService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.SmsFlashPromotionSessionDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购场次管理
 *
 * @author lzq
 * @date 2024/03/01
 */
@Api(value = "SmsFlashPromotionSessionController", tags = "限时购场次管理")
@RestController
@RequestMapping("/flashSession")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsFlashPromotionSessionController {

    private final SmsFlashPromotionSessionService flashPromotionSessionService;

    @ApiOperation("添加场次")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody SmsFlashPromotionSession promotionSession) {
        int count = flashPromotionSessionService.create(promotionSession);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改场次")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsFlashPromotionSession promotionSession) {
        int count = flashPromotionSessionService.update(id, promotionSession);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改启用状态")
    @PostMapping(value = "/update/status/{id}")
    public CommonResult updateStatus(@PathVariable Long id, Integer status) {
        int count = flashPromotionSessionService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除场次")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = flashPromotionSessionService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取场次详情")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsFlashPromotionSession> getItem(@PathVariable Long id) {
        SmsFlashPromotionSession promotionSession = flashPromotionSessionService.getItem(id);
        return CommonResult.success(promotionSession);
    }

    @ApiOperation("获取全部场次")
    @GetMapping(value = "/list")
    public CommonResult<List<SmsFlashPromotionSession>> list() {
        List<SmsFlashPromotionSession> promotionSessionList = flashPromotionSessionService.list();
        return CommonResult.success(promotionSessionList);
    }

    @ApiOperation("获取全部可选场次及其数量")
    @GetMapping(value = "/selectList")
    public CommonResult<List<SmsFlashPromotionSessionDetail>> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> promotionSessionList = flashPromotionSessionService.selectList(flashPromotionId);
        return CommonResult.success(promotionSessionList);
    }
}
