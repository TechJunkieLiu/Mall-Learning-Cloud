package com.aiyangniu.gate.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;

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
}
