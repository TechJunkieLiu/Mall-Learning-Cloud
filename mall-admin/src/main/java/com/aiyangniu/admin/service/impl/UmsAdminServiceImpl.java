package com.aiyangniu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aiyangniu.admin.mapper.UmsAdminMapper;
import com.aiyangniu.admin.mapper.UmsAdminRoleRelationMapper;
import com.aiyangniu.admin.service.UmsAdminService;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台用户实现类
 *
 * @author lzq
 * @date 2023/09/21
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsAdminServiceImpl implements UmsAdminService {

    private final UmsAdminMapper umsAdminMapper;
    private final UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    @Override
    public UserDTO loadUserByUsername(String username){
        // 获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsRole> roleList = getRoleList(admin.getId());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(admin, userDTO);
            if(CollUtil.isNotEmpty(roleList)){
                List<String> roleStrList = roleList.stream().map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
                userDTO.setRoles(roleStrList);
            }
            return userDTO;
        }
        return null;
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        List<UmsAdmin> adminList = umsAdminMapper.selectList(new LambdaQueryWrapper<UmsAdmin>().eq(UmsAdmin::getUsername, username));
        if (adminList != null && adminList.size() > 0) {
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsAdminRoleRelationMapper.getRoleList(adminId);
    }
}
