package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.ums.UmsMemberLevel;

import java.util.List;

/**
 * 会员等级管理接口
 *
 * @author lzq
 * @date 2024/02/20
 */
public interface UmsMemberLevelService {

    /**
     * 获取所有会员等级
     *
     * @param defaultStatus 是否为默认会员
     * @return 会员等级列表
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
