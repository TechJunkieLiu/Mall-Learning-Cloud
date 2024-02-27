package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.UmsResourceService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.ums.UmsResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 后台资源管理
 *
 * @author lzq
 * @date 2024/02/27
 */
@Api(value = "UmsResourceController", tags = "后台资源管理")
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsResourceController {

    private final UmsResourceService umsResourceService;

    @ApiOperation("添加后台资源")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody UmsResource umsResource) {
        int count = umsResourceService.create(umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改后台资源")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsResource umsResource) {
        int count = umsResourceService.update(id, umsResource);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据ID获取资源详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = umsResourceService.getItem(id);
        return CommonResult.success(umsResource);
    }

    @ApiOperation("根据ID删除后台资源")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = umsResourceService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("分页模糊查询后台资源")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<UmsResource>> list(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String nameKeyword,
            @RequestParam(required = false) String urlKeyword,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<UmsResource> resourceList = umsResourceService.list(categoryId, nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(resourceList));
    }

    @ApiOperation("查询所有后台资源")
    @GetMapping(value = "/listAll")
    public CommonResult<List<UmsResource>> listAll() {
        List<UmsResource> resourceList = umsResourceService.listAll();
        return CommonResult.success(resourceList);
    }

    @ApiOperation("初始化资源角色关联数据")
    @GetMapping(value = "/initResourceRolesMap")
    public CommonResult initResourceRolesMap() {
        Map<String, List<String>> resourceRolesMap = umsResourceService.initResourceRolesMap();
        return CommonResult.success(resourceRolesMap);
    }
}
