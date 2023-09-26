package com.aiyangniu.search.mapper;

import com.aiyangniu.search.domain.EsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 搜索商品管理Mapper
 *
 * @author lzq
 * @date 2023/09/25
 */
public interface EsProductMapper {

    /**
     * 获取指定ID的搜索商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
