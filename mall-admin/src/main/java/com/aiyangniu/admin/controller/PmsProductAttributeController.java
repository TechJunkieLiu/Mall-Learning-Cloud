package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.PmsProductAttributeService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.PmsProductAttrInfo;
import com.aiyangniu.entity.model.dto.PmsProductAttributeParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品属性管理
 *
 * @author lzq
 * @date 2024/01/29
 */
@Api(value = "PmsProductAttributeController", tags = "商品属性管理")
@RestController
@RequestMapping("/productAttribute")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PmsProductAttributeController {

    private final PmsProductAttributeService pmsProductAttributeService;

    @ApiOperation("根据分类查询属性列表或参数列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "0表示属性，1表示参数", required = true, paramType = "query", dataType = "integer")})
    @GetMapping(value = "/list/{cid}")
    public CommonResult<CommonPage<PmsProductAttribute>> getList(
            @PathVariable Long cid,
            @RequestParam(value = "type") Integer type,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<PmsProductAttribute> productAttributeList = pmsProductAttributeService.getList(cid, type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeList));
    }

    @ApiOperation("添加商品属性信息")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody PmsProductAttributeParamDTO dto) {
        int count = pmsProductAttributeService.create(dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改商品属性信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductAttributeParamDTO dto) {
        int count = pmsProductAttributeService.update(id, dto);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询单个商品属性")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsProductAttribute> getItem(@PathVariable Long id) {
        PmsProductAttribute productAttribute = pmsProductAttributeService.getItem(id);
        return CommonResult.success(productAttribute);
    }

    @ApiOperation("批量删除商品属性")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = pmsProductAttributeService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据商品分类的id获取商品属性及属性分类")
    @GetMapping(value = "/attrInfo/{productCategoryId}")
    public CommonResult<List<PmsProductAttrInfo>> getAttrInfo(@PathVariable Long productCategoryId) {
        List<PmsProductAttrInfo> productAttrInfoList = pmsProductAttributeService.getProductAttrInfo(productCategoryId);
        return CommonResult.success(productAttrInfoList);
    }
}
