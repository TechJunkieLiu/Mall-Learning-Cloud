package com.aiyangniu.gate.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.api.ResultCode;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.mapper.UmsMemberMapper;
import com.aiyangniu.gate.service.AuthService;
import com.aiyangniu.gate.service.UmsMemberCacheService;
import com.aiyangniu.gate.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
