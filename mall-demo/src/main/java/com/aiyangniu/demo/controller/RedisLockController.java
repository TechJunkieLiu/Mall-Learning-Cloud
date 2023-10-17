package com.aiyangniu.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁
 *
 * @author lzq
 * @date 2023/10/17
 */
@Slf4j
@Api(value = "RedisLockController", tags = "Redis分布式锁")
@RestController
@RequestMapping("/redisLock")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RedisLockController {

    /** 总商品数量 **/
    private int totalNum = 10;
    /** 锁过期时间3秒 **/
    private int lockTimeOut = 3;
    /** 用户抢购的模拟时间（毫秒） **/
    private int userPatient = 30000;
    /** 抢购人数 **/
    private int userNum = 100000;

    private final StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "秒杀系统示例")
    @GetMapping("/monitor")
    public List<String> monitor() {
        // 初始化抢购用户
        List<String> users = initUsers();
        // 抢购成功用户结果表
        List<String> winners = new ArrayList<>();
        // parallelStream方法模拟并发
        users.parallelStream().forEach(user -> {
            // 用户尝试抢购
            String currentUser = secKill(user);
            // 如果抢购成功，则将用户放入结果表
            if (!StringUtils.isEmpty(currentUser)) {
                winners.add(currentUser);
            }
        });
        System.out.println(winners);
        return winners;
    }

    List<String> initUsers() {
        List<String> result = new ArrayList<>();
        // 数字代指用户
        for (int i = 1; i <= userNum; i++) {
            result.add(String.valueOf(i));
        }
        return result;
    }

    /**
     * 在获取锁前后都做了库存数量的判断，以免在库存为0时继续抢购（想象双11抢东西，抢着抢着没货了）
     */
    public String secKill(String user){
        // 用户开抢时间
        long startTime = System.currentTimeMillis();
        // 模拟用户持续抢
        while ((startTime + userPatient) >= System.currentTimeMillis()){
            if (totalNum < 1){
                return null;
            }
            String uuid = UUID.randomUUID().toString();
            Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, lockTimeOut, TimeUnit.SECONDS);
            // 获取锁成功
            if (lock){
                if (totalNum < 1){
                    return null;
                }
                // 模拟用户生成订单时间
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("用户 {} 抢购成功", user);
                totalNum--;
                // 使用LUA脚本执行原子操作，避免锁误删
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptText(script);
                // 设置返回值类型，因为删除判断的时候，默认返回String类型，给其封装为数据类型
                redisScript.setResultType(Long.class);
                stringRedisTemplate.execute(redisScript, Collections.singletonList("lock"), uuid);
                return user;
            }else {
                log.error("用户 {} 抢购失败", user);
            }
        }
        return null;
    }

    @ApiOperation(value = "Redis实现分布式锁示例")
    @GetMapping("/secKill")
    public void secKill() {
        // 唯一字符串或者业务标识
        String uuid = UUID.randomUUID().toString();
        // 设置键过期时间防止死锁
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (lock) {
            log.info("成功获取锁");
            String numStr = stringRedisTemplate.opsForValue().get("num");
            if (StringUtils.isEmpty(numStr)) {
                stringRedisTemplate.opsForValue().set("num", "0");
            } else {
                int number = Integer.parseInt(numStr);
                stringRedisTemplate.opsForValue().set("num", ++number + "");
            }
            // 判断要释放的锁是否是自己的锁
            if (uuid.equals(stringRedisTemplate.opsForValue().get("lock"))){
                // 释放锁
                stringRedisTemplate.delete("lock");
            }
        }else {
            log.info("未获取到锁");
        }
    }
}
