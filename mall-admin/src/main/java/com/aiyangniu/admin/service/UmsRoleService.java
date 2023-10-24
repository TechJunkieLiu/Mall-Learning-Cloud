package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.ums.UmsMenu;
import com.aiyangniu.entity.model.pojo.ums.UmsResource;
import com.aiyangniu.entity.model.pojo.ums.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户角色接口
 *
 * @author lzq
 * @date 2023/10/10
 */
public interface UmsRoleService {

    /**
     * 添加角色
     *
     * @param role 角色
     * @return 添加个数
     */
    int create(UmsRole role);

    /**
     * 修改角色信息
     *
     * @param id 角色ID
     * @param role 角色
     * @return 修改个数
     */
    int update(Long id, UmsRole role);

    /**
     * 批量删除角色
     *
     * @param ids 角色IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    List<UmsRole> list();

    /**
     * 分页获取角色列表
     *
     * @param keyword 关键字
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 角色列表
     */
    List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 根据后台用户ID获取菜单
     *
     * @param adminId 用户ID
     * @return 菜单
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * 获取角色相关菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<UmsMenu> listMenu(Long roleId);

    /**
     * 获取角色相关资源
     *
     * @param roleId 角色ID
     * @return 资源列表
     */
    List<UmsResource> listResource(Long roleId);

    /**
     * 给角色分配菜单
     *
     * @param roleId 角色ID
     * @param menuIds 菜单IDS
     * @return 分配个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    int allocMenu(Long roleId, List<Long> menuIds);

    /**
     * 给角色分配资源
     *
     * @param roleId 角色ID
     * @param resourceIds 资源IDS
     * @return 分配个数
     */
    @Transactional(rollbackFor = RuntimeException.class)
    int allocResource(Long roleId, List<Long> resourceIds);
}
