package com.aiyangniu.gate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.api.ResultCode;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.entity.model.pojo.ums.UmsMemberLevel;
import com.aiyangniu.gate.mapper.UmsMemberLevelMapper;
import com.aiyangniu.gate.mapper.UmsMemberMapper;
import com.aiyangniu.gate.service.AuthService;
import com.aiyangniu.gate.service.UmsMemberCacheService;
import com.aiyangniu.gate.service.UmsMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 会员管理实现类
 *
 * @author lzq
 * @date 2023/09/26
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberServiceImpl implements UmsMemberService {

    private final AuthService authService;
    private final HttpServletRequest request;
    private final UmsMemberMapper umsMemberMapper;
    private final UmsMemberLevelMapper umsMemberLevelMapper;
    private final UmsMemberCacheService umsMemberCacheService;

    @Override
    public CommonResult login(String username, String password) {
        if(StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
            Asserts.fail("用户名或密码不能为空！");
        }
        Map<String, String> params = new HashMap<>(16);
        params.put("client_id", AuthConstant.GATE_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", password);
        return authService.getAccessToken(params);
    }

    @Override
    public UmsMember getCurrentMember() {
        String userStr = request.getHeader(AuthConstant.USER_TOKEN_HEADER);
        if(StrUtil.isEmpty(userStr)){
            Asserts.fail(ResultCode.UNAUTHORIZED);
        }
        UserDTO userDTO = JSONUtil.toBean(userStr, UserDTO.class);
        UmsMember member = umsMemberCacheService.getMember(userDTO.getId());
        if (member == null) {
            member = getById(userDTO.getId());
            umsMemberCacheService.setMember(member);
        }
        return member;
    }

    @Override
    public UmsMember getById(Long id) {
        return umsMemberMapper.selectById(id);
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember record = new UmsMember();
        record.setId(id);
        record.setIntegration(integration);
        umsMemberMapper.updateById(record);
        umsMemberCacheService.delMember(id);
    }

    @Override
    public void register(String username, String password, String telephone, String authCode) {
        // 验证验证码
        if (!verifyAuthCode(authCode, telephone)){
            Asserts.fail("验证码错误");
        }
        // 查询是否已有该用户
        LambdaQueryWrapper<UmsMember> lqw = new LambdaQueryWrapper<UmsMember>().eq(UmsMember::getUsername, username).or().eq(UmsMember::getPhone, telephone);
        List<UmsMember> umsMemberList = umsMemberMapper.selectList(lqw);
        if (!CollectionUtils.isEmpty(umsMemberList)){
            Asserts.fail("该用户已经存在");
        }
        // 没有该用户进行添加操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPhone(telephone);
        umsMember.setPassword(BCrypt.hashpw(password));
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        // 获取默认会员等级并设置
        LambdaQueryWrapper<UmsMemberLevel> lambdaQueryWrapper = new LambdaQueryWrapper<UmsMemberLevel>().eq(UmsMemberLevel::getDefaultStatus, 1);
        List<UmsMemberLevel> umsMemberLevelList = umsMemberLevelMapper.selectList(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(umsMemberLevelList)) {
            umsMember.setMemberLevelId(umsMemberLevelList.get(0).getId());
        }
        umsMemberMapper.insert(umsMember);
        umsMember.setPassword(null);
    }

    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        umsMemberCacheService.setAuthCode(telephone, sb.toString());
        return sb.toString();
    }

    @Override
    public void updatePassword(String telephone, String password, String authCode) {
        List<UmsMember> umsMemberList = umsMemberMapper.selectList(new LambdaQueryWrapper<UmsMember>().eq(UmsMember::getPhone, telephone));
        if(CollectionUtils.isEmpty(umsMemberList)){
            Asserts.fail("该账号不存在");
        }
        // 验证验证码
        if(!verifyAuthCode(authCode, telephone)){
            Asserts.fail("验证码错误");
        }
        UmsMember umsMember = umsMemberList.get(0);
        umsMember.setPassword(BCrypt.hashpw(password));
        umsMemberMapper.updateById(umsMember);
        umsMemberCacheService.delMember(umsMember.getId());
    }

    @Override
    public UserDTO loadUserByUsername(String username) {
        UmsMember member = getByUsername(username);
        if(ObjectUtil.isNotEmpty(member)){
            UserDTO userDTO = new UserDTO();
            BeanUtil.copyProperties(member, userDTO);
            userDTO.setRoles(CollUtil.toList("前台会员"));
            return userDTO;
        }
        return null;
    }

    private UmsMember getByUsername(String username) {
        List<UmsMember> umsMemberList = umsMemberMapper.selectList(new LambdaQueryWrapper<UmsMember>().eq(UmsMember::getUsername, username));
        if (!CollectionUtils.isEmpty(umsMemberList)) {
            return umsMemberList.get(0);
        }
        return null;
    }

    /**
     * 校验验证码
     */
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StrUtil.isEmpty(authCode)){
            return false;
        }
        String realAuthCode = umsMemberCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }

}
