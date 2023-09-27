package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.gate.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员登录注册管理
 *
 * @author lzq
 * @date 2023/09/26
 */
@Api(value = "UmsMemberController", tags = "会员登录注册管理")
@RestController
@RequestMapping("/sso")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberController {

    private final UmsMemberService umsMemberService;

    @ApiOperation("会员登录")
    @PostMapping(value = "/login")
    public CommonResult login(@RequestParam String username, @RequestParam String password) {
        return umsMemberService.login(username, password);
    }
}
