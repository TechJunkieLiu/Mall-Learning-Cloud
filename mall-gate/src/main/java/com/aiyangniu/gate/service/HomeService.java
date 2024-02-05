package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.HomeContentResult;
import com.aiyangniu.entity.model.pojo.cms.CmsSubject;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import com.aiyangniu.entity.model.pojo.pms.PmsProductCategory;

import java.util.List;

/**
 * 首页内容管理接口
 *
 * @author lzq
 * @date 2024/01/31
 */
public interface HomeService {

    /**
     * 获取首页内容
     *
     * @return 首页内容
     */
    HomeContentResult content();

    /**
     * 首页商品推荐
     *
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 商品推荐列表
     */
    List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum);

    /**
     * 获取商品分类
     *
     * @param parentId 0:获取一级分类 其他:获取指定二级分类
     * @return 商品分类列表
     */
    List<PmsProductCategory> getProductCateList(Long parentId);

    /**
     * 根据专题分类分页获取专题
     *
     * @param cateId 专题分类id
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 商品专题列表
     */
    List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum);

    /**
     * 分页获取人气推荐商品
     *
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 人气推荐商品列表
     */
    List<PmsProduct> hotProductList(Integer pageNum, Integer pageSize);

    /**
     * 分页获取新品推荐商品
     *
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 新品推荐商品列表
     */
    List<PmsProduct> newProductList(Integer pageNum, Integer pageSize);
}
