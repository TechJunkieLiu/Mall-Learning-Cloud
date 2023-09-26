package com.aiyangniu.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 *
 * @author lzq
 * @date 2023/09/25
 */
@Configuration
@MapperScan("com.aiyangniu.search.mapper")
//@MapperScan({"com.aiyangniu.search.mapper","com.aiyangniu.gate.mapper","com.aiyangniu.admin.mapper"})
public class MyBatisConfig {
}
