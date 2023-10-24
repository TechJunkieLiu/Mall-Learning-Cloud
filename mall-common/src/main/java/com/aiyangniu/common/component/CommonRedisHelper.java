package com.aiyangniu.common.component;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁工具类
 *
 * @author lzq
 * @date 2023/10/24
 */
@Component
public class CommonRedisHelper {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加分布式锁
     */
    public boolean setNx(String track, String sector, long timeout) {
        boolean flag = false;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        flag = valueOperations.setIfAbsent(track + sector, System.currentTimeMillis());
        if (flag) {
            valueOperations.set(track + sector, getLockValue(track, sector), timeout, TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 删除锁
     *
     * @param lockPrefix 前缀
     * @param key        键
     */
    public void delete(String lockPrefix, String key) {
        redisTemplate.delete(lockPrefix + key);
    }

    /**
     * 查询锁
     *
     * @return 写锁时间
     */
    public long getLockValue(String track, String sector) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        return (long) valueOperations.get(track + sector);
    }
}
