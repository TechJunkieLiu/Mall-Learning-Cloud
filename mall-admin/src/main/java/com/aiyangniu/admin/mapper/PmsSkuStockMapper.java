package com.aiyangniu.admin.mapper;

import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品SKU库存管理Mapper
 *
 * @author lzq
 * @date 2024/01/29
 */
public interface PmsSkuStockMapper extends BaseMapper<PmsSkuStock> {

    /**
     * 批量创建
     *
     * @param skuStockList 产品满减列表
     * @return 创建个数
     */
    int insertList(@Param("list") List<PmsSkuStock> skuStockList);
}
