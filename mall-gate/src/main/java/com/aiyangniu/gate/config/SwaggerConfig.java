package com.aiyangniu.gate.config;

import com.aiyangniu.common.config.BaseSwaggerConfig;
import com.aiyangniu.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 *
 * @author lzq
 * @date 2023/09/26
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.aiyangniu.gate.controller")
                .title("Mall 前台系统")
                .description("Mall 前台相关接口文档")
                .contactName("lzq")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
