package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.ums.UmsAdminLoginLog;

/**
 * 用户登录日志Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface UmsAdminLoginLogMapper {

    /**
     * 添加用户登录记录
     *
     * @param record 用户登录记录
     * @return 添加个数
     */
    int insert(UmsAdminLoginLog record);
}
