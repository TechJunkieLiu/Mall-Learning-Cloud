package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.SmsFlashPromotionProduct;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionProductRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 限时购商品关联管理接口
 *
 * @author lzq
 * @date 2024/02/29
 */
public interface SmsFlashPromotionProductRelationService {

    /**
     * 批量添加关联
     *
     * @param relationList 限时购商品关联
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int create(List<SmsFlashPromotionProductRelation> relationList);

    /**
     * 修改关联信息
     *
     * @param id 限时购活动ID
     * @param relation 关联信息
     * @return 修改个数
     */
    int update(Long id, SmsFlashPromotionProductRelation relation);

    /**
     * 删除单个关联
     *
     * @param id 关联ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 获取关联详情
     *
     * @param id 关联ID
     * @return 关联详情
     */
    SmsFlashPromotionProductRelation getItem(Long id);

    /**
     * 分页查询相关商品及限时购促销信息
     *
     * @param flashPromotionId        限时购id
     * @param flashPromotionSessionId 限时购场次id
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 限时购商品信息列表
     */
    List<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum);

    /**
     * 根据活动和场次id获取商品关系数量
     *
     * @param flashPromotionId        限时购id
     * @param flashPromotionSessionId 限时购场次id
     * @return 商品关系数量
     */
    long getCount(Long flashPromotionId, Long flashPromotionSessionId);
}
