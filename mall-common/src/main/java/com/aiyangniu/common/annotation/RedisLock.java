package com.aiyangniu.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义Redis分布式锁注解
 * AOP处理定时任务，多台同个任务类似抢占，先抢到的则打标识记录在Redis中，根据有无标识去执行任务
 *
 * @author lzq
 * @date 2023/10/24
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     * 锁前缀
     */
    String lockPrefix() default "redisLock:";

    /**
     * 键
     */
    String lockKey() default "";

    /**
     * 默认超时时间 秒
     */
    long timeOut() default 6;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
