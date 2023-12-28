package com.aiyangniu.demo.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos测试类
 *
 * @author lzq
 * @date 2023/12/27
 */
@Api(value = "NacosTestController", tags = "Nacos测试类")
@RefreshScope
@RestController
@RequestMapping("/nacos")
public class NacosTestController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @RequestMapping("/getMessage")
    public String getMessage(){
        System.out.println(url);
        System.out.println(username);
        System.out.println(password);
        return "url:" + url + "</br>username:" + username + "</br>password:" + password;
    }
}
