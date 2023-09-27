package com.aiyangniu.demo.fallback;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.service.FeignGateService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * mall-gate 熔断降级处理类
 *
 * @author lzq
 * @date 2023/09/27
 */
@Component
public class FeignGateFallbackFactory implements FallbackFactory<FeignGateService> {

    @Override
    public FeignGateService create(Throwable throwable) {
        return new FeignGateService() {
            @Override
            public CommonResult login(String username, String password) {
                return CommonResult.failed(throwable.getMessage());
            }
            @Override
            public CommonResult list() {
                return CommonResult.failed(throwable.getMessage());
            }
        };
    }
}
