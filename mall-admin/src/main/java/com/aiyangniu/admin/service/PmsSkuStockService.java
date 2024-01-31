package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;

import java.util.List;

/**
 * 商品SKU库存管理接口
 *
 * @author lzq
 * @date 2024/01/31
 */
public interface PmsSkuStockService {

    /**
     * 根据产品id和skuCode关键字模糊搜索
     *
     * @param pid 商品ID
     * @param keyword skuCode关键字
     * @return SKU库存列表
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     *
     * @param pid 商品ID
     * @param skuStockList 商品库存列表
     * @return 更新个数
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
