package com.aiyangniu.gate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * mall-gate 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@MapperScan("com.aiyangniu.gate.mapper")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.aiyangniu")
public class MallGateApp {
    public static void main(String[] args) {
        SpringApplication.run(MallGateApp.class, args);
    }
}
