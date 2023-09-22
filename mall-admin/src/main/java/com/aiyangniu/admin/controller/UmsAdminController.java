package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.UmsAdminService;
import com.aiyangniu.common.domain.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理
 *
 * @author lzq
 * @date 2023/09/21
 */
@Api(value = "UmsAdminController", tags = "后台用户管理")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsAdminController {

    private final UmsAdminService umsAdminService;

    @ApiOperation("根据用户名获取通用用户信息")
    @GetMapping(value = "/loadByUsername")
    public UserDTO loadUserByUsername(@RequestParam String username) {
        UserDTO userDTO = umsAdminService.loadUserByUsername(username);
        return userDTO;
    }
}