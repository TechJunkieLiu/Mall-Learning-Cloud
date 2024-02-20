package com.aiyangniu.admin.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Spring缓存相关配置
 *
 * @author lzq
 * @date 2024/02/18
 */
@Configuration
public class SpringCacheConfig {

    /**
     * key生成策略，用于Dao层Mapper接口注解，可根据需要在业务系统自定义缓存key生成策略
     */
    @Bean
    public KeyGenerator interfaceKeyGenerator() {
        return (targetClass, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            // 获取真实的接口名称
            sb.append(targetClass.getClass().getInterfaces()[0].getName()).append(".").append(method.getName());
            for (Object obj : objects) {
                sb.append(".").append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * key生成策略，用于Service层Impl类注解，可根据需要在业务系统自定义缓存key生成策略
     */
    @Bean
    public KeyGenerator classKeyGenerator() {
        return (targetClass, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            // 获取真实的接口名称
            sb.append(targetClass.getClass().getName()).append(".").append(method.getName());
            for (Object obj : objects) {
                sb.append(".").append(obj.toString());
            }
            return sb.toString();
        };
    }

    @Bean
    public KeyGenerator keyGenerator(){
        return new KeyGenerator(){
            @Override
            public Object generate(Object target, Method method, Object... params) {
                return method.getName() + "[" + Arrays.asList(params).toString() + "]";
            }
        };
    }
}
