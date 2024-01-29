package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.PmsProductCategoryService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.PmsProductCategoryWithChildrenItem;
import com.aiyangniu.entity.model.dto.PmsProductCategoryParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类管理
 *
 * @author lzq
 * @date 2024/01/29
 */
@Api(value = "PmsProductCategoryController", tags = "商品分类管理")
@RestController
@RequestMapping("/productCategory")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductCategoryController {

    private final PmsProductCategoryService pmsProductCategoryService;

    @ApiOperation("添加商品分类")
    @PostMapping(value = "/create")
    public CommonResult create(@Validated @RequestBody PmsProductCategoryParamDTO dto) {
        int count = pmsProductCategoryService.create(dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改商品分类")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @Validated @RequestBody PmsProductCategoryParamDTO dto) {
        int count = pmsProductCategoryService.update(id, dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页查询商品分类")
    @GetMapping(value = "/list/{parentId}")
    public CommonResult<CommonPage<PmsProductCategory>> getList(
            @PathVariable Long parentId,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<PmsProductCategory> productCategoryList = pmsProductCategoryService.getList(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productCategoryList));
    }

    @ApiOperation("根据id获取商品分类")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsProductCategory> getItem(@PathVariable Long id) {
        PmsProductCategory productCategory = pmsProductCategoryService.getItem(id);
        return CommonResult.success(productCategory);
    }

    @ApiOperation("删除商品分类")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = pmsProductCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改导航栏显示状态")
    @PostMapping(value = "/update/navStatus")
    public CommonResult updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        int count = pmsProductCategoryService.updateNavStatus(ids, navStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改显示状态")
    @PostMapping(value = "/update/showStatus")
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = pmsProductCategoryService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping(value = "/list/withChildren")
    public CommonResult<List<PmsProductCategoryWithChildrenItem>> listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = pmsProductCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
}
