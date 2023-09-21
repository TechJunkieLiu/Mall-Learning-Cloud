package com.aiyangniu.auth.service.impl;

import com.aiyangniu.auth.constant.MessageConstant;
import com.aiyangniu.auth.domain.SecurityUser;
import com.aiyangniu.auth.service.UmsAdminService;
import com.aiyangniu.auth.service.UmsMemberService;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.domain.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理业务类
 *
 * @author lzq
 * @date 2023/09/21
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserDetailsService {

    private final UmsAdminService adminService;
    private final UmsMemberService memberService;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");
        UserDTO dto;
        if(AuthConstant.ADMIN_CLIENT_ID.equals(clientId)){
            dto = adminService.loadUserByUsername(username);
        }else{
            dto = memberService.loadUserByUsername(username);
        }
        if (dto == null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        dto.setClientId(clientId);
        SecurityUser securityUser = new SecurityUser(dto);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }
}
