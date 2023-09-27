package com.aiyangniu.demo.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.service.FeignGateService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign调用mall-gate接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@Api(value = "FeignGateController", tags = "Feign调用mall-gate接口示例")
@RestController
@RequestMapping("/feign/gate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FeignGateController {

    private final FeignGateService feignGateService;

    @PostMapping("/login")
    public CommonResult login(@RequestParam String username, @RequestParam String password) {
        return feignGateService.login(username,password);
    }

    @GetMapping("/cartList")
    public CommonResult cartList() {
        return feignGateService.list();
    }
}
