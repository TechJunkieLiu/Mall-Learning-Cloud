package com.aiyangniu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.UmsAdminLoginLogMapper;
import com.aiyangniu.admin.mapper.UmsAdminMapper;
import com.aiyangniu.admin.mapper.UmsAdminRoleRelationMapper;
import com.aiyangniu.admin.service.AuthService;
import com.aiyangniu.admin.service.UmsAdminService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.api.ResultCode;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import com.aiyangniu.entity.model.pojo.ums.UmsAdminLoginLog;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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

    private final AuthService authService;
    private final UmsAdminMapper umsAdminMapper;
    private final UmsAdminLoginLogMapper umsAdminLoginLogMapper;
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

    @Override
    public String login(String username, String password) {
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        Map<String, String> params = new HashMap<>(16);
        params.put("client_id", AuthConstant.ADMIN_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        CommonResult restResult = authService.getAccessToken(params);
        if(ResultCode.SUCCESS.getCode() == restResult.getCode() && restResult.getData() != null){
            insertLoginLog(username);
        }
        return (String) restResult.getData();
    }

    /**
     * 添加登录记录
     */
    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if(ObjectUtil.isEmpty(admin)) {
            return;
        }
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        umsAdminLoginLogMapper.insert(loginLog);
    }
}
