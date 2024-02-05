package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.HomeContentResult;
import com.aiyangniu.entity.model.pojo.cms.CmsSubject;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import com.aiyangniu.gate.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页内容管理
 *
 * @author lzq
 * @date 2024/01/31
 */
@Api(value = "HomeController", tags = "首页内容管理")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HomeController {

    private final HomeService homeService;

    @ApiOperation("首页内容信息展示")
    @GetMapping(value = "/content")
    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }

    @ApiOperation("分页获取推荐商品")
    @GetMapping(value = "/recommendProductList")
    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }

    @ApiOperation("获取首页商品分类")
    @GetMapping(value = "/productCateList/{parentId}")
    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }

    @ApiOperation("根据分类获取专题")
    @GetMapping(value = "/subjectList")
    public CommonResult<List<CmsSubject>> getSubjectList(
            @RequestParam(required = false) Long cateId,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId, pageSize, pageNum);
        return CommonResult.success(subjectList);
    }

    @ApiOperation("分页获取人气推荐商品")
    @GetMapping(value = "/hotProductList")
    public CommonResult<List<PmsProduct>> hotProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.hotProductList(pageNum, pageSize);
        return CommonResult.success(productList);
    }

    @ApiOperation("分页获取新品推荐商品")
    @GetMapping(value = "/newProductList")
    public CommonResult<List<PmsProduct>> newProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize) {
        List<PmsProduct> productList = homeService.newProductList(pageNum, pageSize);
        return CommonResult.success(productList);
    }
}
