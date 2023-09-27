package com.aiyangniu.demo.fallback;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.service.FeignSearchService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * mall-search 熔断降级处理类
 *
 * @author lzq
 * @date 2023/09/27
 */
@Component
public class FeignSearchFallbackFactory implements FallbackFactory<FeignSearchService> {

    @Override
    public FeignSearchService create(Throwable throwable) {
        return (keyword, pageNum, pageSize) -> CommonResult.failed(throwable.getMessage());
    }
}
