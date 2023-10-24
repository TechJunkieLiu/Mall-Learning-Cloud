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

    @ApiOperation("会员注册")
    @PostMapping(value = "/register")
    public CommonResult register(@RequestParam String username, @RequestParam String password, @RequestParam String telephone, @RequestParam String authCode) {
        umsMemberService.register(username, password, telephone, authCode);
        return CommonResult.success(null,"注册成功");
    }

    @ApiOperation("会员修改密码")
    @PostMapping(value = "/updatePassword")
    public CommonResult updatePassword(@RequestParam String telephone, @RequestParam String password, @RequestParam String authCode) {
        umsMemberService.updatePassword(telephone, password, authCode);
        return CommonResult.success(null,"密码修改成功");
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getAuthCode")
    public CommonResult getAuthCode(@RequestParam String telephone) {
        String authCode = umsMemberService.generateAuthCode(telephone);
        return CommonResult.success(authCode,"获取验证码成功");
    }
}
