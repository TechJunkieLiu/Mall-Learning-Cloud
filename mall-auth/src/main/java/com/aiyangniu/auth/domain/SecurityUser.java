package com.aiyangniu.auth.domain;

import com.aiyangniu.common.domain.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 登录用户信息
 *
 * @author lzq
 * @date 2023/09/21
 */
@Data
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    /** ID **/
    private Long id;
    /** 用户名 **/
    private String username;
    /** 用户密码 **/
    private String password;
    /** 用户状态 **/
    private Boolean enabled;
    /** 登录客户端ID **/
    private String clientId;
    /** 权限数据 **/
    private Collection<SimpleGrantedAuthority> authorities;

    public SecurityUser(UserDTO dto) {
        this.setId(dto.getId());
        this.setUsername(dto.getUsername());
        this.setPassword(dto.getPassword());
        this.setEnabled(dto.getStatus() == 1);
        this.setClientId(dto.getClientId());
        if (dto.getRoles() != null) {
            authorities = new ArrayList<>();
            dto.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
