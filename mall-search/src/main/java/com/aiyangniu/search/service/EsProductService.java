package com.aiyangniu.search.service;

import com.aiyangniu.search.domain.EsProduct;
import com.aiyangniu.search.domain.EsProductRelatedInfo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 搜索商品管理接口
 *
 * @author lzq
 * @date 2023/09/25
 */
public interface EsProductService {

    /**
     * 从数据库中导入所有商品到ES
     *
     * @return 导入数量
     */
    int importAll();

    /**
     * 根据id删除商品
     *
     * @param id 商品ID
     */
    void delete(Long id);

    /**
     * 批量删除商品
     *
     * @param ids 商品ID集合
     */
    void delete(List<Long> ids);

    /**
     * 根据ID创建商品
     *
     * @param id 商品ID
     * @return 商品信息
     */
    EsProduct create(Long id);

    /**
     * 根据关键字搜索名称或者副标题
     *
     * @param keyword 关键字
     * @param pageNum 起始页
     * @param pageSize 页条数
     * @return 分页商品信息
     */
    Page<EsProduct> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据关键字搜索名称或者副标题复合查询
     *
     * @param keyword 关键字
     * @param brandId 品牌ID
     * @param productCategoryId 产品类别ID
     * @param pageNum 起始页
     * @param pageSize 页条数
     * @param sort 排序字段
     * @return 分页商品信息
     */
    Page<EsProduct> search(String keyword, Long brandId, Long productCategoryId, Integer pageNum, Integer pageSize, Integer sort);

    /**
     * 根据商品id推荐相关商品
     *
     * @param id 商品ID
     * @param pageNum 起始页
     * @param pageSize 页条数
     * @return 分页商品信息
     */
    Page<EsProduct> recommend(Long id, Integer pageNum, Integer pageSize);

    /**
     * 获取搜索词相关品牌、分类、属性
     *
     * @param keyword 关键字
     * @return 品牌、分类、属性
     */
    EsProductRelatedInfo searchRelatedInfo(String keyword);
}
