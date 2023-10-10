package com.aiyangniu.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.aiyangniu.admin.mapper.UmsAdminLoginLogMapper;
import com.aiyangniu.admin.mapper.UmsAdminMapper;
import com.aiyangniu.admin.mapper.UmsAdminRoleRelationMapper;
import com.aiyangniu.admin.service.AuthService;
import com.aiyangniu.admin.service.UmsAdminCacheService;
import com.aiyangniu.admin.service.UmsAdminService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.api.ResultCode;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.entity.model.dto.UmsAdminDTO;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import com.aiyangniu.entity.model.pojo.ums.UmsAdminLoginLog;
import com.aiyangniu.entity.model.pojo.ums.UmsAdminRoleRelation;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    private final HttpServletRequest request;
    private final UmsAdminMapper umsAdminMapper;
    private final UmsAdminCacheService umsAdminCacheService;
    private final UmsAdminLoginLogMapper umsAdminLoginLogMapper;
    private final UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;

    @Override
    public UmsAdmin register(UmsAdminDTO dto) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(dto, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        // 查询是否有相同用户名的用户
        List<UmsAdmin> umsAdminList = umsAdminMapper.selectList(new LambdaQueryWrapper<UmsAdmin>().eq(UmsAdmin::getUsername, umsAdmin.getUsername()));
        if (umsAdminList.size() > 0) {
            return null;
        }
        // 将密码进行加密操作
        String encodePassword = BCrypt.hashpw(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        umsAdminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public CommonResult login(String username, String password) {
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
        return restResult;
    }

    @Override
    public UmsAdmin getCurrentAdmin() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        UserDTO dto = JSONUtil.toBean(userStr, UserDTO.class);
        UmsAdmin admin = umsAdminCacheService.getAdmin(dto.getId());
        if (admin == null) {
            admin = umsAdminMapper.selectById(dto.getId());
            umsAdminCacheService.setAdmin(admin);
        }
        return admin;
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        // 先删除原来的关系
        umsAdminRoleRelationMapper.deleteById(adminId);
        // 建立新关系
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            umsAdminRoleRelationMapper.insertList(list);
        }
        return count;
    }

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
