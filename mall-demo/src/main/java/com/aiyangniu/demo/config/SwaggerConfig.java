package com.aiyangniu.demo.config;

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
                .apiBasePackage("com.aiyangniu.demo.controller")
                .title("Mall-Demo 系统")
                .description("SpringCloud 版本中的一些示例")
                .contactName("lzq")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
