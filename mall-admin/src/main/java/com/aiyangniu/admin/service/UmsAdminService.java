package com.aiyangniu.admin.service;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;

import java.util.List;

/**
 * 后台用户接口
 *
 * @author lzq
 * @date 2023/09/21
 */
public interface UmsAdminService {

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    UserDTO loadUserByUsername(String username);

    /**
     * 根据用户名获取后台管理员
     *
     * @param username 用户名
     * @return 后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 获取用户对应角色
     *
     * @param adminId 用户ID
     * @return 角色列表
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的token
     */
    String login(String username, String password);
}
