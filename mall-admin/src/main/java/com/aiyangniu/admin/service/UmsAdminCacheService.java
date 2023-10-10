package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;

/**
 * 后台用户缓存操作接口
 *
 * @author lzq
 * @date 2023/10/10
 */
public interface UmsAdminCacheService {

    /**
     * 获取缓存后台用户信息
     *
     * @param adminId 用户ID
     * @return 用户信息
     */
    UmsAdmin getAdmin(Long adminId);

    /**
     * 设置缓存后台用户信息
     *
     * @param admin 用户信息
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 删除后台用户缓存
     *
     * @param adminId 用户ID
     */
    void delAdmin(Long adminId);
}
