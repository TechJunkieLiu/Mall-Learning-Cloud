package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.UmsRoleMapper;
import com.aiyangniu.admin.mapper.UmsRoleMenuRelationMapper;
import com.aiyangniu.admin.mapper.UmsRoleResourceRelationMapper;
import com.aiyangniu.admin.service.UmsResourceService;
import com.aiyangniu.admin.service.UmsRoleService;
import com.aiyangniu.entity.model.pojo.ums.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户角色实现类
 *
 * @author lzq
 * @date 2023/10/10
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsRoleServiceImpl implements UmsRoleService {

    private final UmsRoleMapper umsRoleMapper;
    private final UmsResourceService resourceService;
    private final UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;
    private final UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Override
    public int create(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        return umsRoleMapper.insert(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return umsRoleMapper.updateById(role);
    }

    @Override
    public int delete(List<Long> ids) {
        int count = umsRoleMapper.delete(new LambdaQueryWrapper<UmsRole>().in(UmsRole::getId, ids));
        resourceService.initResourceRolesMap();
        return count;
    }

    @Override
    public List<UmsRole> list() {
        return umsRoleMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return umsRoleMapper.selectList(new LambdaQueryWrapper<UmsRole>().like(!StrUtil.isEmpty(keyword), UmsRole::getName, keyword));
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return umsRoleMapper.getMenuList(adminId);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return umsRoleMapper.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return umsRoleMapper.getResourceListByRoleId(roleId);
    }

    @Override
    public int allocMenu(Long roleId, List<Long> menuIds) {
        // 先删除原有关系
        umsRoleMenuRelationMapper.delete(new LambdaQueryWrapper<UmsRoleMenuRelation>().eq(UmsRoleMenuRelation::getRoleId, roleId));
        // 批量插入新关系
        List<UmsRoleMenuRelation> list = new ArrayList<>();
        menuIds.forEach(e -> {
            UmsRoleMenuRelation umsRoleMenuRelation = new UmsRoleMenuRelation();
            umsRoleMenuRelation.setRoleId(roleId);
            umsRoleMenuRelation.setMenuId(e);
            list.add(umsRoleMenuRelation);
        });
        return umsRoleMenuRelationMapper.insertList(list);
    }

    @Override
    public int allocResource(Long roleId, List<Long> resourceIds) {
        // 先删除原有关系
        umsRoleResourceRelationMapper.delete(new LambdaQueryWrapper<UmsRoleResourceRelation>().eq(UmsRoleResourceRelation::getRoleId, roleId));
        // 批量插入新关系
        List<UmsRoleResourceRelation> list = new ArrayList<>();
        resourceIds.forEach(e -> {
            UmsRoleResourceRelation umsRoleMenuRelation = new UmsRoleResourceRelation();
            umsRoleMenuRelation.setRoleId(roleId);
            umsRoleMenuRelation.setResourceId(e);
            list.add(umsRoleMenuRelation);
        });
        int count = umsRoleResourceRelationMapper.insertList(list);
        resourceService.initResourceRolesMap();
        return count;
    }
}
