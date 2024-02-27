package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.UmsMenuNode;
import com.aiyangniu.entity.model.pojo.ums.UmsMenu;

import java.util.List;

/**
 * 后台菜单管理接口
 *
 * @author lzq
 * @date 2024/02/26
 */
public interface UmsMenuService {

    /**
     * 创建后台菜单
     *
     * @param umsMenu 菜单
     * @return 创建个数
     */
    int create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     *
     * @param id 菜单ID
     * @param umsMenu 修改菜单
     * @return 修改个数
     */
    int update(Long id, UmsMenu umsMenu);

    /**
     * 根据ID获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    UmsMenu getItem(Long id);

    /**
     * 根据ID删除菜单
     *
     * @param id 菜单ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 分页查询后台菜单
     *
     * @param parentId 父菜单ID
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 菜单列表
     */
    List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 树形结构返回所有菜单列表
     *
     * @return 菜单列表
     */
    List<UmsMenuNode> treeList();

    /**
     * 修改菜单显示状态
     *
     * @param id 菜单ID
     * @param hidden 显示状态
     * @return 修改个数
     */
    int updateHidden(Long id, Integer hidden);
}
