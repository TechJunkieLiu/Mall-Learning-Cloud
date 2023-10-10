package com.aiyangniu.admin.component;

import com.aiyangniu.admin.service.UmsResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 资源与角色访问对应关系操作组件
 *
 * @author lzq
 * @date 2023/10/10
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceRoleRulesHolder {

    private final UmsResourceService resourceService;

    @PostConstruct
    public void initResourceRolesMap(){
        resourceService.initResourceRolesMap();
    }
}
