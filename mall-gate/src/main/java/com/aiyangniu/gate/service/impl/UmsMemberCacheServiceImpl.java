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
    private String redisDatabase;

    @Value("${redis.expire.common}")
    private Long redisExpire;

    @Value("${redis.expire.authCode}")
    private Long redisExpireAuthCode;

    @Value("${redis.key.member}")
    private String redisKeyMember;

    @Value("${redis.key.authCode}")
    private String redisKeyAuthCode;

    @Override
    public UmsMember getMember(Long memberId) {
        String key = redisDatabase + ":" + redisKeyMember + ":" + memberId;
        return (UmsMember) redisService.get(key);
    }

    @Override
    public void setMember(UmsMember member) {
        String key = redisDatabase + ":" + redisKeyMember + ":" + member.getId();
        redisService.set(key, member, redisExpire);
    }
}
