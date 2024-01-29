package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.PmsProductAttrInfo;
import com.aiyangniu.entity.model.dto.PmsProductAttributeParamDTO;
import com.aiyangniu.entity.model.pojo.pms.PmsProductAttribute;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品属性管理接口
 *
 * @author lzq
 * @date 2024/01/29
 */
public interface PmsProductAttributeService {

    /**
     * 根据分类分页获取商品属性
     *
     * @param cid 分类id
     * @param type 0->规格；1->参数
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 商品属性列表
     */
    List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 添加商品属性
     *
     * @param dto 商品属性
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(PmsProductAttributeParamDTO dto);

    /**
     * 修改商品属性
     *
     * @param id 属性ID
     * @param dto 商品属性
     * @return 修改个数
     */
    int update(Long id, PmsProductAttributeParamDTO dto);

    /**
     * 获取单个商品属性信息
     *
     * @param id 属性ID
     * @return 商品属性信息
     */
    PmsProductAttribute getItem(Long id);

    /**
     * 批量删除商品属性
     *
     * @param ids 属性IDS
     * @return 删除个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int delete(List<Long> ids);

    /**
     * 获取商品分类对应属性列表
     *
     * @param productCategoryId 商品分类ID
     * @return 属性列表
     */
    List<PmsProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
