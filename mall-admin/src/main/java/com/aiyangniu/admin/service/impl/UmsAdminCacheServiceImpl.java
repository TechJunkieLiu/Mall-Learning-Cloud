package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.service.UmsAdminCacheService;
import com.aiyangniu.common.service.RedisService;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 后台用户缓存操作实现类
 *
 * @author lzq
 * @date 2023/10/10
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    private final RedisService redisService;

    @Value("${redis.database}")
    private String redisDatabase;

    @Value("${redis.expire.common}")
    private Long redisExpire;

    @Value("${redis.key.admin}")
    private String redisKeyAdmin;

    @Override
    public UmsAdmin getAdmin(Long adminId) {
        String key = redisDatabase + ":" + redisKeyAdmin + ":" + adminId;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = redisDatabase + ":" + redisKeyAdmin + ":" + admin.getId();
        redisService.set(key, admin, redisExpire);
    }

    @Override
    public void delAdmin(Long adminId) {
        String key = redisDatabase + ":" + redisKeyAdmin + ":" + adminId;
        redisService.del(key);
    }
}
