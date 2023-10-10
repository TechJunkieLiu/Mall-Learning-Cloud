package com.aiyangniu.admin.config;

import com.aiyangniu.common.config.BaseSwaggerConfig;
import com.aiyangniu.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Swagger API文档相关配置
 *
 * @author lzq
 * @date 2023/09/26
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.aiyangniu.admin.controller")
                .title("Mall 后台系统")
                .description("Mall 后台相关接口文档")
                .contactName("lzq")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
