package com.aiyangniu.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aiyangniu.admin.mapper.UmsResourceMapper;
import com.aiyangniu.admin.mapper.UmsRoleMapper;
import com.aiyangniu.admin.mapper.UmsRoleResourceRelationMapper;
import com.aiyangniu.admin.service.UmsResourceService;
import com.aiyangniu.common.constant.AuthConstant;
import com.aiyangniu.common.service.RedisService;
import com.aiyangniu.entity.model.pojo.ums.UmsResource;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.aiyangniu.entity.model.pojo.ums.UmsRoleResourceRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台资源管理实现类
 *
 * @author lzq
 * @date 2023/10/10
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsResourceServiceImpl implements UmsResourceService {

    @Value("${spring.application.name}")
    private String applicationName;

    private final RedisService redisService;
    private final UmsRoleMapper roleMapper;
    private final UmsResourceMapper umsResourceMapper;
    private final UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;

    @Override
    public int create(UmsResource umsResource) {
        umsResource.setCreateTime(new Date());
        int count = umsResourceMapper.insert(umsResource);
        initResourceRolesMap();
        return count;
    }

    @Override
    public int update(Long id, UmsResource umsResource) {
        umsResource.setId(id);
        int count = umsResourceMapper.updateById(umsResource);
        initResourceRolesMap();
        return count;
    }

    @Override
    public UmsResource getItem(Long id) {
        return umsResourceMapper.selectById(id);
    }

    @Override
    public int delete(Long id) {
        int count = umsResourceMapper.deleteById(id);
        initResourceRolesMap();
        return count;
    }

    @Override
    public List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return umsResourceMapper.selectList(new LambdaQueryWrapper<UmsResource>()
                .eq(categoryId != null, UmsResource::getCategoryId, categoryId)
                .like(StrUtil.isNotEmpty(nameKeyword), UmsResource::getName, nameKeyword)
                .like(StrUtil.isNotEmpty(urlKeyword), UmsResource::getUrl, urlKeyword)
        );
    }

    @Override
    public List<UmsResource> listAll() {
        return umsResourceMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public Map<String, List<String>> initResourceRolesMap() {
        Map<String, List<String>> resourceRoleMap = new TreeMap<>();
        List<UmsResource> resourceList = umsResourceMapper.selectList(new LambdaQueryWrapper<>());
        List<UmsRole> roleList = roleMapper.selectList(new LambdaQueryWrapper<>());
        List<UmsRoleResourceRelation> relationList = umsRoleResourceRelationMapper.selectList(new LambdaQueryWrapper<>());
        for (UmsResource resource : resourceList) {
            Set<Long> roleIds = relationList.stream().filter(item -> item.getResourceId().equals(resource.getId())).map(UmsRoleResourceRelation::getRoleId).collect(Collectors.toSet());
            List<String> roleNames = roleList.stream().filter(item -> roleIds.contains(item.getId())).map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
            resourceRoleMap.put("/" + applicationName + resource.getUrl(), roleNames);
        }
        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRoleMap);
        return resourceRoleMap;
    }
}
