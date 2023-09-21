package com.aiyangniu.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录用户信息
 *
 * @author lzq
 * @date 2023/09/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDTO {

    /** ID **/
    private Long id;
    /** 用户名 **/
    private String username;
    /** 用户密码 **/
    private String password;
    /** 用户状态 **/
    private Integer status;
    /** 登录客户端ID **/
    private String clientId;
    /** 角色列表 **/
    private List<String> roles;
}
