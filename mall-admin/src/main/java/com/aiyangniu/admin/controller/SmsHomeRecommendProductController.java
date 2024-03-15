package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsHomeRecommendProductService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页人气推荐管理
 *
 * @author lzq
 * @date 2024/03/15
 */
@Api(value = "SmsHomeRecommendProductController", tags = "首页人气推荐管理")
@RestController
@RequestMapping("/home/recommendProduct")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeRecommendProductController {

    private final SmsHomeRecommendProductService recommendProductService;

    @ApiOperation("添加首页推荐")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody List<SmsHomeRecommendProduct> homeRecommendProductList) {
        int count = recommendProductService.create(homeRecommendProductList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping(value = "/update/sort/{id}")
    public CommonResult updateSort(@PathVariable Long id, Integer sort) {
        int count = recommendProductService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = recommendProductService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = recommendProductService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeRecommendProduct>> list(
            @RequestParam(value = "productName", required = false) String productName,
            @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsHomeRecommendProduct> homeRecommendProductList = recommendProductService.list(productName, recommendStatus, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(homeRecommendProductList));
    }
}
