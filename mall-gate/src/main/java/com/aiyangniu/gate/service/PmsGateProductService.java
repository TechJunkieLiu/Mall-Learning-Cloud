package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.pojo.pms.PmsProduct;

import java.util.List;

/**
 * 前台商品管理接口
 *
 * @author lzq
 * @date 2024/08/30
 */
public interface PmsGateProductService {

    /**
     * 综合搜索商品
     *
     * @param keyword 关键字
     * @param brandId 品牌ID
     * @param productCategoryId 商品分类ID
     * @param pageNum 当前页
     * @param pageSize 页条数
     * @param sort 排序字段
     * @return 商品列表
     */
    List<PmsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);
}
