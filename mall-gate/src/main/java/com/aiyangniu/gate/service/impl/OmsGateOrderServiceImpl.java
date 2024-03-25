package com.aiyangniu.gate.service.impl;

import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.entity.model.bo.*;
import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductCategoryRelation;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponProductRelation;
import com.aiyangniu.entity.model.pojo.ums.UmsIntegrationConsumeSetting;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.entity.model.pojo.ums.UmsMemberReceiveAddress;
import com.aiyangniu.gate.component.CancelOrderSender;
import com.aiyangniu.gate.mapper.*;
import com.aiyangniu.gate.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 前台订单管理实现类
 *
 * @author lzq
 * @date 2024/03/22
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
    private final OmsCartItemService omsCartItemService;
    private final UmsMemberReceiveAddressService umsMemberReceiveAddressService;
    private final UmsMemberCouponService umsMemberCouponService;
    private final UmsIntegrationConsumeSettingMapper umsIntegrationConsumeSettingMapper;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> cartIds) {
        ConfirmOrderResult result = new ConfirmOrderResult();
        // 获取购物车信息
        UmsMember currentMember = umsMemberService.getCurrentMember();
        List<CartPromotionItem> itemList = omsCartItemService.listPromotion(currentMember.getId(), cartIds);
        result.setCartPromotionItemList(itemList);
        // 获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = umsMemberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        // 获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = umsMemberCouponService.listCart(itemList, 1);
        result.setCouponHistoryDetailList(couponHistoryDetailList);
        // 获取用户积分
        result.setMemberIntegration(currentMember.getIntegration());
        // 获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = umsIntegrationConsumeSettingMapper.selectById(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);
        // 计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(itemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        // 获取购物车及优惠信息
        UmsMember currentMember = umsMemberService.getCurrentMember();
        List<CartPromotionItem> itemList = omsCartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        for (CartPromotionItem item : itemList) {
            // 判断下单商品是否都有库存
            if (item.getRealStock() == null || item.getRealStock() <= 0){
                Asserts.fail("库存不足，无法下单！");
            }
            // 生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setProductName(item.getProductName());
            orderItem.setProductPic(item.getProductPic());
            orderItem.setProductAttr(item.getProductAttr());
            orderItem.setProductBrand(item.getProductBrand());
            orderItem.setProductSn(item.getProductSn());
            orderItem.setProductPrice(item.getPrice());
            orderItem.setProductQuantity(item.getQuantity());
            orderItem.setProductSkuId(item.getProductSkuId());
            orderItem.setProductSkuCode(item.getProductSkuCode());
            orderItem.setProductCategoryId(item.getProductCategoryId());
            orderItem.setPromotionAmount(item.getReduceAmount());
            orderItem.setPromotionName(item.getPromotionMessage());
            orderItem.setGiftIntegration(item.getIntegration());
            orderItem.setGiftGrowth(item.getGrowth());
            orderItemList.add(orderItem);
        }
        // 判断是否使用了优惠券
        if (orderParam.getCouponId() == null) {
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(BigDecimal.ZERO);
            }
        } else {
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(itemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("该优惠券不可用！");
            }
            // 对下单商品的优惠券进行处理
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }
        // 判断是否使用积分

















        return null;
    }

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

    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(BigDecimal.ZERO);
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal promotionAmount = BigDecimal.ZERO;
        for (CartPromotionItem item : cartPromotionItemList) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            promotionAmount = promotionAmount.add(item.getReduceAmount().multiply(new BigDecimal(item.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param itemList 购物车优惠列表
     * @param couponId 使用优惠券id
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> itemList, Long couponId) {
        List<SmsCouponHistoryDetail> couponHistoryDetailList = umsMemberCouponService.listCart(itemList, 1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
                return couponHistoryDetail;
            }
        }
        return null;
    }

    /**
     * 对优惠券优惠进行处理
     *
     * @param orderItemList order_item列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            // 全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getUseType().equals(1)) {
            // 指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            // 指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     *
     * @param orderItemList 可用优惠券的下单商品
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            // (商品价格/可用商品总价)*优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 获取与优惠券有关系的下单商品
     *
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList 下单商品
     * @param type 使用关系类型：0->相关分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(productRelation.getProductId());
            }
            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal(0));
                }
            }
        }
        return result;
    }
}
