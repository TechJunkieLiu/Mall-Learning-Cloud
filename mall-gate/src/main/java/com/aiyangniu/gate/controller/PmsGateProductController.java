package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.PmsGateProductDetail;
import com.aiyangniu.entity.model.bo.PmsProductCategoryNode;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.gate.service.PmsGateProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台商品管理
 *
 * @author lzq
 * @date 2024/08/30
 */
@Api(value = "PmsGateProductController", tags = "前台商品管理")
@RestController
@RequestMapping("/gate/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsGateProductController {

    private final PmsGateProductService pmsGateProductService;

    @ApiOperation(value = "综合搜索、筛选、排序")
    @ApiImplicitParam(
            name = "sort", value = "排序字段：0->按相关度；1->按新品；2->按销量；3->价格从低到高；4->价格从高到低", defaultValue = "0", allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer"
    )
    @GetMapping(value = "/search")
    public CommonResult<CommonPage<PmsProduct>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long productCategoryId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize,
            @RequestParam(required = false, defaultValue = "0") Integer sort
    ) {
        List<PmsProduct> productList = pmsGateProductService.search(keyword, brandId, productCategoryId, pageNum, pageSize, sort);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("以树形结构获取所有商品分类")
    @GetMapping(value = "/categoryTreeList")
    public CommonResult<List<PmsProductCategoryNode>> categoryTreeList() {
        List<PmsProductCategoryNode> list = pmsGateProductService.categoryTreeList();
        return CommonResult.success(list);
    }

    @ApiOperation("获取前台商品详情")
    @GetMapping(value = "/detail/{id}")
    public CommonResult<PmsGateProductDetail> detail(@PathVariable Long id) {
        PmsGateProductDetail productDetail = pmsGateProductService.detail(id);
        return CommonResult.success(productDetail);
    }

}
