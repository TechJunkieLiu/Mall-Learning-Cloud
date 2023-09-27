package com.aiyangniu.demo.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.dto.UmsAdminLoginParam;
import com.aiyangniu.demo.service.FeignAdminService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign调用mall-admin接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@Api(value = "FeignAdminController", tags = "Feign调用mall-admin接口示例")
@RestController
@RequestMapping("/feign/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FeignAdminController {

    private final FeignAdminService feignAdminService;

    @PostMapping("/login")
    public CommonResult login(@RequestBody UmsAdminLoginParam loginParam) {
        return feignAdminService.login(loginParam);
    }

    @GetMapping("/getBrandList")
    public CommonResult getBrandList(){
        return feignAdminService.getList();
    }
}
