package com.aiyangniu.search.config;

import com.aiyangniu.common.config.BaseSwaggerConfig;
import com.aiyangniu.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 *
 * @author lzq
 * @date 2023/09/25
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.aiyangniu.search.controller")
                .title("Mall 搜索系统")
                .description("Mall 搜索相关接口文档")
                .contactName("lzq")
                .version("1.0")
                .enableSecurity(false)
                .build();
    }
}
