package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.bo.SmsFlashPromotionSessionDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsFlashPromotionSession;

import java.util.List;

/**
 * 限时购场次管理接口
 *
 * @author lzq
 * @date 2024/03/01
 */
public interface SmsFlashPromotionSessionService {

    /**
     * 添加场次
     *
     * @param promotionSession 限时购场次
     * @return 添加个数
     */
    int create(SmsFlashPromotionSession promotionSession);

    /**
     * 修改场次
     *
     * @param id 限时购场次ID
     * @param promotionSession 限时购场次
     * @return 修改个数
     */
    int update(Long id, SmsFlashPromotionSession promotionSession);

    /**
     * 修改场次启用状态
     *
     * @param id 限时购场次ID
     * @param status 启用状态
     * @return 修改个数
     */
    int updateStatus(Long id, Integer status);

    /**
     * 删除单个场次
     *
     * @param id 限时购场次ID
     * @return 删除个数
     */
    int delete(Long id);

    /**
     * 获取限时购场次详情
     *
     * @param id 限时购场次ID
     * @return 限时购场次详情
     */
    SmsFlashPromotionSession getItem(Long id);

    /**
     * 根据启用状态获取场次列表
     *
     * @return 限时购场次列表
     */
    List<SmsFlashPromotionSession> list();

    /**
     * 获取全部可选场次及其数量
     *
     * @param flashPromotionId 限时购场次ID
     * @return 包含商品数量的场次信息列表
     */
    List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId);
}
