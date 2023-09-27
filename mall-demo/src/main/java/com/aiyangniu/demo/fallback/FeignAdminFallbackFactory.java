package com.aiyangniu.demo.fallback;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.dto.UmsAdminLoginParam;
import com.aiyangniu.demo.service.FeignAdminService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * mall-admin 熔断降级处理类
 *
 * @author lzq
 * @date 2023/09/27
 */
@Component
public class FeignAdminFallbackFactory implements FallbackFactory<FeignAdminService> {

    @Override
    public FeignAdminService create(Throwable throwable) {
        return new FeignAdminService() {
            @Override
            public CommonResult login(UmsAdminLoginParam loginParam) {
                return CommonResult.failed(throwable.getMessage());
            }

            @Override
            public CommonResult getList() {
                return CommonResult.failed(throwable.getMessage());
            }
        };
    }
}
