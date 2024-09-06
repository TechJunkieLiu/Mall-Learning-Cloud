package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.gate.service.PmsGateBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台商品品牌管理
 *
 * @author lzq
 * @date 2024/09/05
 */
@Api(value = "PmsGateBrandController", tags = "前台商品品牌管理")
@RestController
@RequestMapping("/gate/brand")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsGateBrandController {

    private final PmsGateBrandService pmsGateBrandService;

    @ApiOperation("分页获取推荐品牌")
    @GetMapping(value = "/recommendList")
    public CommonResult<List<PmsBrand>> recommendList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize
    ) {
        List<PmsBrand> brandList = pmsGateBrandService.recommendList(pageNum, pageSize);
        return CommonResult.success(brandList);
    }

    @ApiOperation("获取品牌详情")
    @GetMapping(value = "/detail/{brandId}")
    public CommonResult<PmsBrand> detail(@PathVariable Long brandId) {
        PmsBrand brand = pmsGateBrandService.detail(brandId);
        return CommonResult.success(brand);
    }

    @ApiOperation("分页获取品牌相关商品")
    @GetMapping(value = "/productList")
    public CommonResult<CommonPage<PmsProduct>> productList(
            @RequestParam Long brandId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize
    ) {
        CommonPage<PmsProduct> result = pmsGateBrandService.productList(brandId, pageNum, pageSize);
        return CommonResult.success(result);
    }
}
