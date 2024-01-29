package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.PmsProductCategoryWithChildrenItem;
import com.aiyangniu.entity.model.dto.PmsProductCategoryParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类管理接口
 *
 * @author lzq
 * @date 2024/01/29
 */
public interface PmsProductCategoryService {

    /**
     * 创建商品分类
     *
     * @param dto 商品分类
     * @return 创建个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(PmsProductCategoryParamDTO dto);

    /**
     * 修改商品分类
     *
     * @param id 商品分类ID
     * @param dto 商品分类
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int update(Long id, PmsProductCategoryParamDTO dto);

    /**
     * 分页获取商品分类
     *
     * @param parentId 上级分类ID
     * @param pageSize 当前页
     * @param pageNum 页条数
     * @return 商品分类列表
     */
    List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 删除商品分类
     *
     * @param id 分类ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 根据ID获取商品分类
     *
     * @param id 分类ID
     * @return 商品分类信息
     */
    PmsProductCategory getItem(Long id);

    /**
     * 批量修改导航状态
     *
     * @param ids 分类IDS
     * @param navStatus 导航状态
     * @return 修改个数
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 批量修改显示状态
     *
     * @param ids 分类IDS
     * @param showStatus 显示状态
     * @return 修改个数
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 以层级形式获取商品分类
     *
     * @return 商品分类列表
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
