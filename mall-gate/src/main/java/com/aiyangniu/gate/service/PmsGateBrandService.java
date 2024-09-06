package com.aiyangniu.gate.service;

import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;

import java.util.List;

/**
 * 前台商品品牌管理接口
 *
 * @author lzq
 * @date 2024/09/05
 */
public interface PmsGateBrandService {

    /**
     * 分页获取推荐品牌
     *
     * @param pageNum 当前页
     * @param pageSize 页条数
     * @return 推荐品牌列表
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * 获取品牌详情
     *
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    PmsBrand detail(Long brandId);

    /**
     * 分页获取品牌关联商品
     *
     * @param brandId 品牌ID
     * @param pageNum 当前页
     * @param pageSize 页条数
     * @return 品牌关联商品列表
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
