package com.aiyangniu.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 *
 * @author lzq
 * @date 2023/09/28
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.aiyangniu.admin.mapper", "com.aiyangniu.gate.mapper"})
public class MyBatisConfig {
}
