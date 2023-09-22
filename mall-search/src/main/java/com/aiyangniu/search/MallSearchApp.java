package com.aiyangniu.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * mall-search 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@MapperScan("com.aiyangniu.search.mapper")
@EnableDiscoveryClient
@SpringBootApplication
public class MallSearchApp {
    public static void main(String[] args) {
        SpringApplication.run(MallSearchApp.class, args);
    }
}
