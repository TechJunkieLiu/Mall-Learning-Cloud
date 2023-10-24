package com.aiyangniu.gate.service.impl;

import com.aiyangniu.common.annotation.CacheException;
import com.aiyangniu.common.service.RedisService;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.mapper.UmsMemberMapper;
import com.aiyangniu.gate.service.UmsMemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private final UmsMemberMapper umsMemberMapper;

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

    @Override
    public void delMember(Long memberId) {
        UmsMember umsMember = umsMemberMapper.selectById(memberId);
        if (!ObjectUtils.isEmpty(umsMember)) {
            String key = redisDatabase + ":" + redisKeyMember + ":" + umsMember.getUsername();
            redisService.del(key);
        }
    }

    @CacheException
    @Override
    public String getAuthCode(String telephone) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + telephone;
        return (String) redisService.get(key);
    }

    @CacheException
    @Override
    public void setAuthCode(String telephone, String authCode) {
        String key = redisDatabase + ":" + redisKeyAuthCode + ":" + telephone;
        // 模拟代码异常
//        int i = 1 / 0;
        redisService.set(key, authCode, redisExpireAuthCode);
    }
}
