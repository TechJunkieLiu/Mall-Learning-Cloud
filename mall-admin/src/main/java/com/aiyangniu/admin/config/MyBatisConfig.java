package com.aiyangniu.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis相关配置
 *
 * @author lzq
 * @date 2023/09/25
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.aiyangniu.admin.mapper")
public class MyBatisConfig {
}
