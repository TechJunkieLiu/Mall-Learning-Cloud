package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品SKU库存管理Mapper
 *
 * @author lzq
 * @date 2023/09/20
 */
public interface PmsSkuStockMapper extends BaseMapper<PmsSkuStock> {

    /**
     * 获取下单商品的所有库存信息
     *
     * @param productSkuId 下单商品的SKU ID
     * @return 库存信息
     */
    PmsSkuStock selectBySkuId(Long productSkuId);

    /**
     * 锁定下单商品的所有库存
     *
     * @param skuStock 库存信息
     */
    void updatePmsSkuStock(PmsSkuStock skuStock);

    /**
     * 批量插入或替换操作
     *
     * @param skuStockList 商品库存列表
     * @return 更新个数
     */
    int replaceList(@Param("list") List<PmsSkuStock> skuStockList);

    /**
     * 批量创建
     *
     * @param skuStockList 产品满减列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);
}
