package com.aiyangniu.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 初始化restTemplate的配置类
 * 通过@Configuration和@Bean将创建的RestTemplate注入容器中
 *
 * @author lzq
 * @date 2024/02/21
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
