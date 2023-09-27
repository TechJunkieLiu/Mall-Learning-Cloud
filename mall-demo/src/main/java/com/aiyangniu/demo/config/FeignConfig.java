package com.aiyangniu.demo.config;

import com.aiyangniu.demo.component.FeignRequestInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign相关配置
 *
 * @author lzq
 * @date 2023/09/26
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
