package com.aiyangniu.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * mall-admin 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallAdminApp {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApp.class, args);
    }
}
