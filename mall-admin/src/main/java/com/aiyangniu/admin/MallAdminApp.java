package com.aiyangniu.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * mall-admin 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@MapperScan("com.aiyangniu.admin.mapper")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MallAdminApp {
    public static void main(String[] args) {
        SpringApplication.run(MallAdminApp.class, args);
    }
}
