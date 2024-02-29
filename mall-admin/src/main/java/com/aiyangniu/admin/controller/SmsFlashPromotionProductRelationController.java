package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsFlashPromotionProductRelationService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.SmsFlashPromotionProduct;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionProductRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 限时购和商品关系管理
 *
 * @author lzq
 * @date 2024/02/29
 */
@Api(value = "SmsFlashPromotionProductRelationController", tags = "限时购和商品关系管理")
@RestController
@RequestMapping("/flashProductRelation")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsFlashPromotionProductRelationController {

    private final SmsFlashPromotionProductRelationService relationService;

    @ApiOperation("批量选择商品添加关联")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody List<SmsFlashPromotionProductRelation> relationList) {
        int count = relationService.create(relationList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改关联信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsFlashPromotionProductRelation relation) {
        int count = relationService.update(id, relation);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除关联")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = relationService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取关联商品促销信息")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsFlashPromotionProductRelation> getItem(@PathVariable Long id) {
        SmsFlashPromotionProductRelation relation = relationService.getItem(id);
        return CommonResult.success(relation);
    }

    @ApiOperation("分页查询不同场次关联及商品信息")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsFlashPromotionProduct>> list(
            @RequestParam(value = "flashPromotionId") Long flashPromotionId,
            @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsFlashPromotionProduct> flashPromotionProductList = relationService.list(flashPromotionId, flashPromotionSessionId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(flashPromotionProductList));
    }
}
