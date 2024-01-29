package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.PmsBrandService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.dto.PmsBrandParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌管理
 *
 * @author lzq
 * @date 2024/01/26
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

    @ApiOperation(value = "添加品牌")
    @PostMapping(value = "/create")
    public CommonResult create(@Validated @RequestBody PmsBrandParamDTO dto) {
        CommonResult commonResult;
        int count = pmsBrandService.createBrand(dto);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation(value = "更新品牌")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable("id") Long id, @Validated @RequestBody PmsBrandParamDTO dto) {
        CommonResult commonResult;
        int count = pmsBrandService.updateBrand(id, dto);
        if (count == 1) {
            commonResult = CommonResult.success(count);
        } else {
            commonResult = CommonResult.failed();
        }
        return commonResult;
    }

    @ApiOperation(value = "删除品牌")
    @GetMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable("id") Long id) {
        int count = pmsBrandService.deleteBrand(id);
        if (count == 1) {
            return CommonResult.success(null);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "根据品牌名称分页获取品牌列表")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<PmsBrand>> getList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "showStatus",required = false) Integer showStatus,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        List<PmsBrand> brandList = pmsBrandService.listBrand(keyword,showStatus,pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

    @ApiOperation(value = "根据编号查询品牌信息")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsBrand> getItem(@PathVariable("id") Long id) {
        return CommonResult.success(pmsBrandService.getBrand(id));
    }

    @ApiOperation(value = "批量删除品牌")
    @PostMapping(value = "/delete/batch")
    public CommonResult deleteBatch(@RequestParam("ids") List<Long> ids) {
        int count = pmsBrandService.deleteBrand(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "批量更新显示状态")
    @PostMapping(value = "/update/showStatus")
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = pmsBrandService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "批量更新厂家制造商状态")
    @PostMapping(value = "/update/factoryStatus")
    public CommonResult updateFactoryStatus(@RequestParam("ids") List<Long> ids, @RequestParam("factoryStatus") Integer factoryStatus) {
        int count = pmsBrandService.updateFactoryStatus(ids, factoryStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }
}
