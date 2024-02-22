package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.UmsMemberLevelService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.ums.UmsMemberLevel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员等级管理
 *
 * @author lzq
 * @date 2024/02/20
 */
@Api(value = "UmsMemberLevelController", tags = "会员等级管理")
@RestController
@RequestMapping("/memberLevel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberLevelController {

    private final UmsMemberLevelService memberLevelService;

    @ApiOperation("查询所有会员等级")
    @GetMapping(value = "/list")
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        List<UmsMemberLevel> memberLevelList = memberLevelService.list(defaultStatus);
        return CommonResult.success(memberLevelList);
    }
}
