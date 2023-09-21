package com.aiyangniu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * mall-gateway 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@EnableDiscoveryClient
@SpringBootApplication
public class MallGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(MallGatewayApp.class, args);
    }
}
