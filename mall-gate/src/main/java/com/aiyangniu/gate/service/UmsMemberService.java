package com.aiyangniu.gate.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员管理接口
 *
 * @author lzq
 * @date 2023/09/26
 */
public interface UmsMemberService {

    /**
     * 会员登录获取Token
     *
     * @param username 用户名
     * @param password 密码
     * @return Token
     */
    CommonResult login(String username, String password);

    /**
     * 获取当前登录会员
     *
     * @return 会员信息
     */
    UmsMember getCurrentMember();

    /**
     * 根据会员编号获取会员
     *
     * @param id 会员ID
     * @return 会员信息
     */
    UmsMember getById(Long id);

    /**
     * 根据会员id修改会员积分
     *
     * @param id 会员ID
     * @param integration 会员积分
     */
    void updateIntegration(Long id, Integer integration);

    /**
     * 会员注册
     *
     * @param username 用户名
     * @param password 密码
     * @param telephone 手机号
     * @param authCode 验证码
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void register(String username, String password, String telephone, String authCode);

    /**
     * 生成验证码
     *
     * @param telephone 手机号
     * @return 验证码
     */
    String generateAuthCode(String telephone);

    /**
     * 修改密码
     *
     * @param telephone 手机号
     * @param password 密码
     * @param authCode 验证码
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void updatePassword(String telephone, String password, String authCode);

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserDTO loadUserByUsername(String username);
}
