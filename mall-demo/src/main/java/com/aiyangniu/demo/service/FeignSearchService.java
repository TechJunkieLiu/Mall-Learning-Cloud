package com.aiyangniu.demo.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.fallback.FeignSearchFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign调用mall-search接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@FeignClient(value = "mall-search", fallbackFactory = FeignSearchFallbackFactory.class)
public interface FeignSearchService {

    @GetMapping("/esProduct/search/simple")
    CommonResult search(@RequestParam(required = false) String keyword,
                        @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                        @RequestParam(required = false, defaultValue = "5") Integer pageSize);
}
