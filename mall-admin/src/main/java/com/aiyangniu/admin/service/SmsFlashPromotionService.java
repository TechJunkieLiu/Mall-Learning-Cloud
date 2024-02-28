package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotion;

import java.util.List;

/**
 * 限时购活动管理接口
 *
 * @author lzq
 * @date 2024/02/28
 */
public interface SmsFlashPromotionService {

    /**
     * 添加活动
     *
     * @param flashPromotion 限时购活动
     * @return 添加个数
     */
    int create(SmsFlashPromotion flashPromotion);

    /**
     * 修改指定活动
     *
     * @param id 限时购活动ID
     * @param flashPromotion 限时购活动
     * @return 修改个数
     */
    int update(Long id, SmsFlashPromotion flashPromotion);

    /**
     * 删除单个活动
     *
     * @param id 限时购活动ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 修改上下线状态
     *
     * @param id 限时购活动ID
     * @param status 上下线状态
     * @return 修改个数
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取活动详情
     *
     * @param id 限时购活动ID
     * @return 活动详情
     */
    SmsFlashPromotion getItem(Long id);

    /**
     * 分页查询活动
     *
     * @param keyword 关键字
     * @param pageSize 页条数
     * @param pageNum 当前页
     * @return 活动列表
     */
    List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum);
}
