package com.aiyangniu.demo.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.fallback.FeignGateFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign调用mall-gate接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@FeignClient(value = "mall-gate", fallbackFactory = FeignGateFallbackFactory.class)
public interface FeignGateService {

    @PostMapping("/sso/login")
    CommonResult login(@RequestParam String username, @RequestParam String password);

    @GetMapping("/cart/list")
    CommonResult list();
}
