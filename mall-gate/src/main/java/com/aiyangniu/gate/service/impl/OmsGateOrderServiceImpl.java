package com.aiyangniu.gate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.exception.Asserts;
import com.aiyangniu.common.service.RedisService;
import com.aiyangniu.entity.model.bo.*;
import com.aiyangniu.entity.model.pojo.oms.OmsOrder;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderItem;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderSetting;
import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
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
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前台订单管理实现类
 *
 * @author lzq
 * @date 2024/03/22
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsGateOrderServiceImpl implements OmsGateOrderService {

    @Value("${redis.database}")
    private String redisDataBase;

    @Value("${redis.key.orderId}")
    private String redisKeyOrderId;

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
    private final PmsSkuStockMapper pmsSkuStockMapper;
    private final RedisService redisService;

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
        List<CartPromotionItem> cartPromotionItemList = omsCartItemService.listPromotion(currentMember.getId(), orderParam.getCartIds());
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            // 判断下单商品是否都有库存
            if (cartPromotionItem.getRealStock() == null || cartPromotionItem.getRealStock() <= 0){
                Asserts.fail("库存不足，无法下单！");
            }
            // 生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
            orderItemList.add(orderItem);
        }
        // 判断是否使用了优惠券
        if (orderParam.getCouponId() == null) {
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(BigDecimal.ZERO);
            }
        } else {
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                Asserts.fail("该优惠券不可用！");
            }
            // 对下单商品的优惠券进行处理
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }
        // 判断是否使用了积分
        if (orderParam.getUseIntegration() == null || orderParam.getUseIntegration() == 0) {
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setIntegrationAmount(BigDecimal.ZERO);
            }
        } else {
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember, orderParam.getCouponId() != null);
            if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
                Asserts.fail("积分不可用！");
            } else {
                // 可用情况下分摊到可用商品中
                for (OmsOrderItem orderItem : orderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
                    orderItem.setIntegrationAmount(perAmount);
                }
            }
        }
        // 计算order_item的实付金额
        handleRealAmount(orderItemList);
        // 进行库存锁定
        lockStock(cartPromotionItemList);
        // 根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(BigDecimal.ZERO);
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));

        if (orderParam.getCouponId() == null){
            order.setCouponAmount(BigDecimal.ZERO);
        }else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }

        if (orderParam.getUseIntegration() == null){
            order.setIntegration(0);
            order.setIntegrationAmount(BigDecimal.ZERO);
        }else {
            order.setIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }
        order.setPayAmount(calcPayAmount(order));
        // 转化为订单信息并插入数据库
        order.setMemberId(currentMember.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(currentMember.getUsername());
        // 支付方式 0-未支付 1-支付宝 2-微信
        order.setPayType(orderParam.getPayType());
        // 订单来源 0-PC订单 1-APP订单
        order.setSourceType(1);
        // 订单状态 0-待付款 1-待发货 2-已发货 3-已完成 4-已关闭 5-无效订单
        order.setStatus(0);
        // 订单类型 0-正常订单 1-秒杀订单
        order.setOrderType(0);
        // 收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = umsMemberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        // 0-未确认 1-已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        // 计算赠送积分
        order.setIntegration(calcGiftIntegration(orderItemList));
        // 计算赠送成长值
        order.setGrowth(calcGiftGrowth(orderItemList));
        // 生成订单号
        order.setOrderSn(generateOrderSn(order));
        // 设置自动收货天数
        List<OmsOrderSetting> orderSettingList = omsOrderSettingMapper.selectList(new LambdaQueryWrapper<>());
        if (!CollectionUtils.isEmpty(orderSettingList)){
            order.setAutoConfirmDay(orderSettingList.get(0).getConfirmOvertime());
        }
        // 入库订单、订单商品
        omsOrderMapper.insert(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        omsOrderItemMapper.insertList(orderItemList);
        // 更新优惠券使用状态
        if (orderParam.getCouponId() != null){
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }
        // 扣除积分
        if (orderParam.getUseIntegration() != null){
            order.setUseIntegration(orderParam.getUseIntegration());
            umsMemberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
        }
        // 删除购物车下单商品
        deleteCartItemList(cartPromotionItemList, currentMember);
        // 发送延迟消息取消订单
        sendDelayMessageCancelOrder(order.getId());
        Map<String, Object> result = new HashMap<>(16);
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        return result;
    }

    @Override
    public void sendDelayMessageCancelOrder(Long orderId) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = omsOrderSettingMapper.selectById(orderId);
        long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
        // 发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        // 修改订单支付状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        order.setPayType(payType);
        omsOrderMapper.updateById(order);
        // 恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = gateOrderMapper.getDetail(orderId);
        return gateOrderMapper.updateSkuStock(orderDetail.getOrderItemList());
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
    public CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize) {
        if (status == -1){
            status = null;
        }
        UmsMember currentMember = umsMemberService.getCurrentMember();
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<OmsOrder> lqw = new LambdaQueryWrapper<OmsOrder>().eq(OmsOrder::getDeleteStatus, 0).eq(OmsOrder::getMemberId, currentMember.getId()).eq(status != null, OmsOrder::getStatus, status).orderByDesc(OmsOrder::getCreateTime);
        List<OmsOrder> orderList = omsOrderMapper.selectList(lqw);
        CommonPage<OmsOrder> orderPage = CommonPage.restPage(orderList);
        // 设置分页信息
        CommonPage<OmsOrderDetail> resultPage = new CommonPage<>();
        resultPage.setPageNum(orderPage.getPageNum());
        resultPage.setPageSize(orderPage.getPageSize());
        resultPage.setTotal(orderPage.getTotal());
        resultPage.setTotalPage(orderPage.getTotalPage());
        if(CollUtil.isEmpty(orderList)){
            return resultPage;
        }
        // 设置数据信息
        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).collect(Collectors.toList());
        List<OmsOrderItem> orderItemList = omsOrderItemMapper.selectBatchIds(orderIds);
        List<OmsOrderDetail> orderDetailList = new ArrayList<>();
        for (OmsOrder omsOrder : orderList) {
            OmsOrderDetail orderDetail = new OmsOrderDetail();
            BeanUtil.copyProperties(omsOrder, orderDetail);
            List<OmsOrderItem> relatedItemList = orderItemList.stream().filter(item -> item.getOrderId().equals(orderDetail.getId())).collect(Collectors.toList());
            orderDetail.setOrderItemList(relatedItemList);
            orderDetailList.add(orderDetail);
        }
        resultPage.setList(orderDetailList);
        return resultPage;
    }

    @Override
    public OmsOrderDetail detail(Long orderId) {
        OmsOrder order = omsOrderMapper.selectById(orderId);
        List<OmsOrderItem> orderItemList = omsOrderItemMapper.selectList(new LambdaQueryWrapper<OmsOrderItem>().eq(OmsOrderItem::getOrderId, orderId));
        OmsOrderDetail orderDetail = new OmsOrderDetail();
        BeanUtil.copyProperties(order, orderDetail);
        orderDetail.setOrderItemList(orderItemList);
        return orderDetail;
    }

    @Override
    public void confirmReceiveOrder(Long orderId) {
        UmsMember member = umsMemberService.getCurrentMember();
        OmsOrder order = omsOrderMapper.selectById(orderId);
        if (!member.getId().equals(order.getMemberId())){
            Asserts.fail("不能确认他人订单！");
        }
        if (order.getStatus() != 2){
            Asserts.fail("该订单还未发货！");
        }
        order.setStatus(3);
        order.setConfirmStatus(1);
        order.setReceiveTime(new Date());
        omsOrderMapper.updateById(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        UmsMember member = umsMemberService.getCurrentMember();
        OmsOrder order = omsOrderMapper.selectById(orderId);
        if (!member.getId().equals(order.getMemberId())){
            Asserts.fail("不能删除他人订单！");
        }
        if (order.getStatus() == 3 || order.getStatus() == 4){
            order.setDeleteStatus(1);
            omsOrderMapper.updateById(order);
        }else {
            Asserts.fail("只能删除已完成或已关闭的订单！");
        }
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

    /**
     * 获取可用积分抵扣金额
     *
     * @param useIntegration 使用的积分数量
     * @param totalAmount 订单总金额
     * @param currentMember 使用的用户
     * @param hasCoupon 是否已经使用优惠券
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal(0);
        // 判断用户是否有这么多积分
        if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
            return zeroAmount;
        }
        // 根据积分使用规则判断是否可用
        UmsIntegrationConsumeSetting integrationConsumeSetting = umsIntegrationConsumeSettingMapper.selectById(1L);
        // 是否可与优惠券共用
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            return zeroAmount;
        }
        // 是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }
        // 是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration).divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder()).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            // 原价-促销优惠-优惠券抵扣-积分抵扣
            BigDecimal realAmount = orderItem.getProductPrice()
                    .subtract(orderItem.getPromotionAmount())
                    .subtract(orderItem.getCouponAmount())
                    .subtract(orderItem.getIntegrationAmount());
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 锁定下单商品的所有库存
     */
    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            PmsSkuStock skuStock = pmsSkuStockMapper.selectById(cartPromotionItem.getProductSkuId());
            skuStock.setLockStock(skuStock.getLowStock() + cartPromotionItem.getQuantity());
            pmsSkuStockMapper.updateById(skuStock);
        }
    }

    /**
     * 计算订单促销优惠金额
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getPromotionName());
            sb.append(";");
        }
        String result = sb.toString();
        if (result.endsWith(";")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 计算订单优惠券优惠金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算订单积分抵扣金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal(0);
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        // 总金额+运费-促销优惠-优惠券优惠-积分抵扣
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
        return payAmount;
    }

    /**
     * 计算该订单赠送的积分
     */
    private Integer calcGiftIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 计算该订单赠送的成长值
     */
    private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum += orderItem.getGiftGrowth() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 生成18位订单编号：8位日期 + 2位平台号码 + 2位支付方式 + 6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = redisDataBase + ":" + redisKeyOrderId + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            ids.add(cartPromotionItem.getId());
        }
        omsCartItemService.delete(currentMember.getId(), ids);
    }
}
