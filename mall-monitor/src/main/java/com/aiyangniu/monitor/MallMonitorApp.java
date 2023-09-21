package com.aiyangniu.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * mall-monitor 启动类
 *
 * @author lzq
 * @date 2023/09/08
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class MallMonitorApp {
    public static void main(String[] args) {
        SpringApplication.run(MallMonitorApp.class, args);
    }
}
