package com.aiyangniu.gate.service.impl;

import com.aiyangniu.entity.model.bo.OmsOrderDetail;
import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.component.CancelOrderSender;
import com.aiyangniu.gate.mapper.*;
import com.aiyangniu.gate.service.OmsGateOrderService;
import com.aiyangniu.gate.service.UmsMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 前台订单管理实现类
 *
 * @author lzq
 * @date 2023/10/07
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderServiceImpl implements OmsGateOrderService {

    private final OmsOrderSettingMapper omsOrderSettingMapper;
    private final CancelOrderSender cancelOrderSender;
    private final GateOrderMapper gateOrderMapper;
    private final UmsMemberService umsMemberService;
    private final SmsCouponHistoryMapper smsCouponHistoryMapper;
    private final OmsOrderMapper omsOrderMapper;
    private final OmsOrderItemMapper omsOrderItemMapper;

    @Override
    public void cancelOrder(Long orderId) {
        // 查询未付款的取消订单
        List<OmsOrder> cancelOrderList = omsOrderMapper.selectList(new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getId, orderId).eq(OmsOrder::getStatus, 0).eq(OmsOrder::getDeleteStatus, 0));
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return;
        }
        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            // 修改订单状态为取消
            cancelOrder.setStatus(4);
            omsOrderMapper.updateById(cancelOrder);
            List<OmsOrderItem> orderItemList = omsOrderItemMapper.selectList(new LambdaQueryWrapper<OmsOrderItem>().eq(OmsOrderItem::getOrderId, orderId));
            // 解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                gateOrderMapper.releaseSkuStockLock(orderItemList);
            }
            // 修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            // 返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                UmsMember member = umsMemberService.getById(cancelOrder.getMemberId());
                umsMemberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
            }
        }
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = omsOrderSettingMapper.selectById(orderId);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        // 发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     * 将优惠券信息更改为指定状态
     *
     * @param couponId  优惠券ID
     * @param memberId  会员ID
     * @param useStatus 0->未使用 1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }
        // 查询第一张优惠券
        List<SmsCouponHistory> smsCouponHistoryList = smsCouponHistoryMapper.selectList(new LambdaQueryWrapper<SmsCouponHistory>()
                .eq(SmsCouponHistory::getMemberId, memberId)
                .eq(SmsCouponHistory::getCouponId, couponId)
                .eq(SmsCouponHistory::getUseStatus, useStatus == 0 ? 1 : 0)
        );
        if (!CollectionUtils.isEmpty(smsCouponHistoryList)) {
            SmsCouponHistory smsCouponHistory = smsCouponHistoryList.get(0);
            smsCouponHistory.setUseTime(new Date());
            smsCouponHistory.setUseStatus(useStatus);
            smsCouponHistoryMapper.updateById(smsCouponHistory);
        }
    }

    @Override
    public Integer cancelTimeOutOrder() {
        Integer count = 0;
        OmsOrderSetting orderSetting = omsOrderSettingMapper.selectById(1L);

        // 查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeOutOrders = gateOrderMapper.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeOutOrders)) {
            return count;
        }
        // 修改订单状态为交易取消
        List<Long> ids = new ArrayList<>();
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            ids.add(timeOutOrder.getId());
        }
        gateOrderMapper.updateOrderStatus(ids, 4);
        for (OmsOrderDetail timeOutOrder : timeOutOrders) {
            // 解除订单商品库存锁定
            gateOrderMapper.releaseSkuStockLock(timeOutOrder.getOrderItemList());
            // 修改优惠券使用状态
            updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
            // 返还使用积分
            if (timeOutOrder.getUseIntegration() != null) {
                UmsMember member = umsMemberService.getById(timeOutOrder.getMemberId());
                umsMemberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
            }
        }
        return timeOutOrders.size();
    }
}
