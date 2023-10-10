package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.ums.UmsAdminRoleRelation;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台用户与角色关系Mapper
 *
 * @author lzq
 * @date 2023/09/21
 */
public interface UmsAdminRoleRelationMapper extends BaseMapper<UmsAdminRoleRelation> {

    /**
     * 获取用于所有角色
     *
     * @param adminId 用户ID
     * @return 角色列表
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     * 批量插入新的用户角色关系
     *
     * @param list 用户角色列表
     */
    void insertList(@Param("list") List<UmsAdminRoleRelation> list);
}
