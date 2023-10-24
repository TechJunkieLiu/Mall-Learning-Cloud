package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.pojo.ums.UmsMember;

/**
 * 会员信息缓存接口
 *
 * @author lzq
 * @date 2023/09/26
 */
public interface UmsMemberCacheService {

    /**
     * 获取会员用户缓存
     *
     * @param memberId 会员编号
     * @return 会员缓存信息
     */
    UmsMember getMember(Long memberId);

    /**
     * 设置会员用户缓存
     *
     * @param member 会员信息
     */
    void setMember(UmsMember member);

    /**
     * 删除会员用户缓存
     *
     * @param memberId 会员ID
     */
    void delMember(Long memberId);

    /**
     * 获取验证码
     *
     * @param telephone 手机号
     * @return 验证码
     */
    String getAuthCode(String telephone);

    /**
     * 设置验证码
     *
     * @param telephone 手机号
     * @param authCode 验证码
     */
    void setAuthCode(String telephone, String authCode);
}
