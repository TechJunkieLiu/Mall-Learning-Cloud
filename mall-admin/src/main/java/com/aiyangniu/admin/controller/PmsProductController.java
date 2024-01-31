package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.PmsProductService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.PmsProductResult;
import com.aiyangniu.entity.model.dto.PmsProductParamDTO;
import com.aiyangniu.entity.model.dto.PmsProductQueryParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理
 *
 * @author lzq
 * @date 2024/01/26
 */
@Api(value = "PmsProductController", tags = "商品管理")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductController {

    private final PmsProductService productService;

    @ApiOperation("创建商品")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody PmsProductParamDTO dto) {
        int count = productService.create(dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @GetMapping(value = "/updateInfo/{id}")
    public CommonResult<PmsProductResult> getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = productService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }

    @ApiOperation("更新商品")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductParamDTO dto) {
        int count = productService.update(id, dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询商品")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<PmsProduct>> getList(
            PmsProductQueryParamDTO dto,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<PmsProduct> productList = productService.list(dto, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("根据商品名称或货号模糊查询")
    @GetMapping(value = "/simpleList")
    public CommonResult<List<PmsProduct>> getList(String keyword) {
        List<PmsProduct> productList = productService.list(keyword);
        return CommonResult.success(productList);
    }

    @ApiOperation("批量修改审核状态")
    @PostMapping(value = "/update/verifyStatus")
    public CommonResult updateVerifyStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("verifyStatus") Integer verifyStatus,
            @RequestParam("detail") String detail
    ) {
        int count = productService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量上下架商品")
    @PostMapping(value = "/update/publishStatus")
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids, @RequestParam("publishStatus") Integer publishStatus) {
        int count = productService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量推荐商品")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = productService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量设为新品")
    @PostMapping(value = "/update/newStatus")
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids, @RequestParam("newStatus") Integer newStatus) {
        int count = productService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("批量修改删除状态")
    @PostMapping(value = "/update/deleteStatus")
    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids, @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
