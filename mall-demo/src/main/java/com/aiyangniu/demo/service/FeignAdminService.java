package com.aiyangniu.demo.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.dto.UmsAdminLoginDTO;
import com.aiyangniu.demo.fallback.FeignAdminFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign调用mall-admin接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@FeignClient(value = "mall-admin", fallbackFactory = FeignAdminFallbackFactory.class)
public interface FeignAdminService {

    @PostMapping("/admin/login")
    CommonResult login(@RequestBody UmsAdminLoginDTO loginParam);

    @GetMapping("/brand/listAll")
    CommonResult getList();
}
