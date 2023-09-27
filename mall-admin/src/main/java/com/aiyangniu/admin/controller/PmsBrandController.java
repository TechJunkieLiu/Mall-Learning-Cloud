package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.PmsBrandService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理
 *
 * @author lzq
 * @date 2023/09/27
 */
@Api(value = "PmsBrandController", tags = "商品品牌管理")
@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsBrandController {

    private final PmsBrandService pmsBrandService;

    @ApiOperation(value = "获取全部品牌列表")
    @GetMapping(value = "/listAll")
    public CommonResult<List<PmsBrand>> getList() {
        return CommonResult.success(pmsBrandService.listAllBrand());
    }
}
