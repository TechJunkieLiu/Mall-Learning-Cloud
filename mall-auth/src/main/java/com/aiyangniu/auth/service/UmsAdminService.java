package com.aiyangniu.auth.service;

import com.aiyangniu.common.domain.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户管理接口
 *
 * @author lzq
 * @date 2023/09/21
 */
@FeignClient("mall-admin")
public interface UmsAdminService {

    /**
     * 获取管理用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/admin/loadByUsername")
    UserDTO loadUserByUsername(@RequestParam String username);
}
