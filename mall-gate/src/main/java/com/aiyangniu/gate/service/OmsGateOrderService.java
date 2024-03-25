package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.ConfirmOrderResult;
import com.aiyangniu.entity.model.bo.OrderParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 前台订单管理接口
 *
 * @author lzq
 * @date 2024/03/22
 */
public interface OmsGateOrderService {

    /**
     * 根据用户购物车信息生成确认单信息
     *
     * @param cartIds 购物车ID
     * @return 确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    /**
     * 根据提交信息生成订单
     *
     * @param orderParam 生成订单入参
     * @return 返回
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    Map<String, Object> generateOrder(OrderParam orderParam);

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
