package com.aiyangniu.admin.service;

import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;

/**
 * 订单设置管理接口
 *
 * @author lzq
 * @date 2024/01/19
 */
public interface OmsOrderSettingService {

    /**
     * 获取指定订单设置
     *
     * @param id 订单设置ID
     * @return 订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     *
     * @param id 订单设置ID
     * @param orderSetting 订单设置
     * @return 修改个数
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
