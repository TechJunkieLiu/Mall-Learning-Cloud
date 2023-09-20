package com.aiyangniu.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * mall-auth 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.aiyangniu")
public class MallAuthApp {
    public static void main(String[] args) {
        SpringApplication.run(MallAuthApp.class, args);
    }
}
