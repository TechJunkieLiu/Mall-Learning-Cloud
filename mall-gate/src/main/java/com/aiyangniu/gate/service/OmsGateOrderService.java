package com.aiyangniu.gate.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * 前台订单管理接口
 *
 * @author lzq
 * @date 2023/10/07
 */
public interface OmsGateOrderService {

    /**
     * 取消单个超时订单
     *
     * @param orderId 订单ID
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     *
     * @param orderId 订单编号
     */
    void sendDelayMessageCancelOrder(Long orderId);

    /**
     * 自动取消超时订单
     *
     * @return 取消数量
     */
    @Transactional(rollbackFor = RuntimeException.class)
    Integer cancelTimeOutOrder();
}
