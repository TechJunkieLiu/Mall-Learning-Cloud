package com.aiyangniu.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.aiyangniu.admin.service.UmsAdminService;
import com.aiyangniu.admin.service.UmsRoleService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.domain.UserDTO;
import com.aiyangniu.entity.model.dto.UmsAdminDTO;
import com.aiyangniu.entity.model.dto.UmsAdminLoginDTO;
import com.aiyangniu.entity.model.pojo.ums.UmsAdmin;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final UmsRoleService umsRoleService;

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public CommonResult<UmsAdmin> register(@Validated @RequestBody UmsAdminDTO dto) {
        UmsAdmin umsAdmin = umsAdminService.register(dto);
        if (umsAdmin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "登录以后返回token")
    @PostMapping(value = "/login")
    public CommonResult login(@Validated @RequestBody UmsAdminLoginDTO dto) {
        return umsAdminService.login(dto.getUsername(), dto.getPassword());
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "/info")
    public CommonResult getAdminInfo() {
        UmsAdmin umsAdmin = umsAdminService.getCurrentAdmin();
        Map<String, Object> data = new HashMap<>(16);
        data.put("username", umsAdmin.getUsername());
        data.put("menus", umsRoleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = umsAdminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return CommonResult.success(data);
    }

    @ApiOperation("登出功能")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation("给用户分配角色")
    @PostMapping(value = "/role/update")
    public CommonResult updateRole(@RequestParam("adminId") Long adminId, @RequestParam("roleIds") List<Long> roleIds) {
        int count = umsAdminService.updateRole(adminId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取指定用户的角色")
    @GetMapping(value = "/role/{adminId}")
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = umsAdminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("根据用户名获取通用用户信息")
    @GetMapping(value = "/loadByUsername")
    public UserDTO loadUserByUsername(@RequestParam String username) {
        return umsAdminService.loadUserByUsername(username);
    }

}
