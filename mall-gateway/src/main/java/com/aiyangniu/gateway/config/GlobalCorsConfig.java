package com.aiyangniu.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 全局跨域配置（注意：前端从网关进行调用时需要配置）
 *
 * @author lzq
 * @date 2023/09/22
 */
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        // 1、添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 放行哪些请求方式
        config.addAllowedMethod("*");
        // 放行哪些原始域
        config.addAllowedOrigin("*");
        // 放行哪些原始请求头部信息
        config.addAllowedHeader("*");
        // 是否发送Cookie
        config.setAllowCredentials(true);
        // 2、添加映射路径
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        // 3、返回新的CorsFilter
        return new CorsWebFilter(source);
    }
}
