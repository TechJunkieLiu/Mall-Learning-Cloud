package com.aiyangniu.gate.config;

import com.aiyangniu.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis相关配置
 *
 * @author lzq
 * @date 2023/09/26
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
