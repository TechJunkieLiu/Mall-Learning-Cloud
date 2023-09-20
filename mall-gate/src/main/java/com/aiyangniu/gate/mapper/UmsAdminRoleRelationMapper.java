package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.ums.UmsAdminRoleRelation;
import com.aiyangniu.entity.model.pojo.ums.UmsResource;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户与角色关系Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface UmsAdminRoleRelationMapper extends BaseMapper<UmsAdminRoleRelation> {

    /**
     * 获取用户所有可访问资源
     *
     * @param adminId 用户ID
     * @return 可访问资源列表
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取用于所有角色
     *
     * @param adminId 用户ID
     * @return 角色列表
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 删除原有用户角色关系
     *
     * @param adminId 用户ID
     */
    void deleteById(@Param("adminId") Long adminId);

    /**
     * 批量插入新的用户角色关系
     *
     * @param list 用户角色列表
     */
    void insertList(@Param("list") List<UmsAdminRoleRelation> list);

    /**
     * 获取资源相关用户ID列表
     *
     * @param resourceId 资源ID
     * @return 用户ID列表
     */
    List<Long> getAdminIdList(@Param("resourceId") Long resourceId);
}
