package com.aiyangniu.gate.mapper;

import com.aiyangniu.entity.model.bo.FlashPromotionProduct;
import com.aiyangniu.entity.model.pojo.cms.CmsSubject;
import com.aiyangniu.entity.model.pojo.pms.PmsBrand;
import com.aiyangniu.entity.model.pojo.pms.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 首页内容管理Mapper
 *
 * @author lzq
 * @date 2024/01/31
 */
public interface HomeMapper{

    /**
     * 获取推荐品牌
     *
     * @param offset offset 偏移量 = pageSize * (pageNum - 1)
     * @param limit 页条数 = pageSize
     * @return 推荐品牌列表
     */
    List<PmsBrand> getRecommendBrandList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取秒杀商品
     *
     * @param flashPromotionId 秒杀ID
     * @param sessionId 秒杀场次ID
     * @return 秒杀商品列表
     */
    List<FlashPromotionProduct> getFlashProductList(@Param("flashPromotionId") Long flashPromotionId, @Param("sessionId") Long sessionId);

    /**
     * 获取新品推荐
     *
     * @param offset offset 偏移量 = pageSize * (pageNum - 1)
     * @param limit 页条数 = pageSize
     * @return 新品推荐列表
     */
    List<PmsProduct> getNewProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取人气推荐
     *
     * @param offset offset 偏移量 = pageSize * (pageNum - 1)
     * @param limit 页条数 = pageSize
     * @return 人气推荐列表
     */
    List<PmsProduct> getHotProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取推荐专题
     *
     * @param offset offset 偏移量 = pageSize * (pageNum - 1)
     * @param limit 页条数 = pageSize
     * @return 推荐专题列表
     */
    List<CmsSubject> getRecommendSubjectList(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
