package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.ums.UmsResource;

import java.util.List;
import java.util.Map;

/**
 * 后台资源管理接口
 *
 * @author lzq
 * @date 2023/10/10
 */
public interface UmsResourceService {

    /**
     * 添加资源
     *
     * @param umsResource 资源
     * @return 添加个数
     */
    int create(UmsResource umsResource);

    /**
     * 修改资源
     *
     * @param id 资源ID
     * @param umsResource 资源
     * @return 修改个数
     */
    int update(Long id, UmsResource umsResource);

    /**
     * 获取资源详情
     *
     * @param id 资源ID
     * @return 资源详情
     */
    UmsResource getItem(Long id);

    /**
     * 删除资源
     *
     * @param id 资源ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 分页查询资源
     *
     * @param categoryId 资源分类ID
     * @param nameKeyword 资源名称关键词
     * @param urlKeyword 资源URL关键词
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 菜单列表
     */
    List<UmsResource> list(Long categoryId, String nameKeyword, String urlKeyword, Integer pageSize, Integer pageNum);

    /**
     * 查询全部资源
     *
     * @return 资源列表
     */
    List<UmsResource> listAll();

    /**
     * 初始化资源角色规则
     *
     * @return 资源角色规则
     */
    Map<String, List<String>> initResourceRolesMap();
}
