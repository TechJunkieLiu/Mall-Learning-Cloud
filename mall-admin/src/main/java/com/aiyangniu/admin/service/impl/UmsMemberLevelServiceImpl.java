package com.aiyangniu.admin.service.impl;

import com.aiyangniu.admin.mapper.UmsMemberLevelMapper;
import com.aiyangniu.admin.service.UmsMemberLevelService;
import com.aiyangniu.entity.model.pojo.ums.UmsMemberLevel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员等级管理实现类
 *
 * @author lzq
 * @date 2024/02/20
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {

    private final UmsMemberLevelMapper memberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        return memberLevelMapper.selectList(new LambdaQueryWrapper<UmsMemberLevel>().eq(UmsMemberLevel::getDefaultStatus, defaultStatus));
    }
}
