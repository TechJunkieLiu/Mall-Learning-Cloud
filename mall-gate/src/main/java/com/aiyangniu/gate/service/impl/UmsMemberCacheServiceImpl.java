package com.aiyangniu.gate.service.impl;

import com.aiyangniu.common.service.RedisService;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.service.UmsMemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 会员信息缓存实现类
 *
 * @author lzq
 * @date 2023/09/26
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberCacheServiceImpl implements UmsMemberCacheService {

    private final RedisService redisService;

    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;

    @Value("${redis.expire.authCode}")
    private Long REDIS_EXPIRE_AUTH_CODE;

    @Value("${redis.key.member}")
    private String REDIS_KEY_MEMBER;

    @Value("${redis.key.authCode}")
    private String REDIS_KEY_AUTH_CODE;

    @Override
    public UmsMember getMember(Long memberId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + memberId;
        return (UmsMember) redisService.get(key);
    }

    @Override
    public void setMember(UmsMember member) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_MEMBER + ":" + member.getId();
        redisService.set(key, member, REDIS_EXPIRE);
    }
}
