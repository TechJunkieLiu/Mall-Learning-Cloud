package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.sms.SmsHomeNewProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页新品推荐管理接口
 *
 * @author lzq
 * @date 2024/03/15
 */
public interface SmsHomeNewProductService {

    /**
     * 添加首页推荐
     *
     * @param homeNewProductList 首页推荐列表
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(List<SmsHomeNewProduct> homeNewProductList);

    /**
     * 修改推荐排序
     *
     * @param id 新品推荐ID
     * @param sort 新品推荐排序
     * @return 修改个数
     */
    int updateSort(Long id, Integer sort);

    /**
     * 批量删除推荐
     *
     * @param ids 新品推荐IDS
     * @return 删除个数
     */
    int delete(List<Long> ids);

    /**
     * 批量更新推荐状态
     *
     * @param ids 新品推荐IDS
     * @param recommendStatus 新品推荐状态
     * @return 更新个数
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 分页查询推荐
     *
     * @param productName 商品名称
     * @param recommendStatus 新品推荐状态
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 首页推荐新品分页列表
     */
    List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
