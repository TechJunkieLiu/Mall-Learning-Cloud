package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.bo.SmsCouponHistoryDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户优惠券管理接口
 *
 * @author lzq
 * @date 2024/03/22
 */
public interface UmsMemberCouponService {

    /**
     * 根据购物车信息获取可用优惠券
     *
     * @param cartItemList 购物车信息
     * @param type 1
     * @return 可用优惠券
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type);

    /**
     * 会员添加优惠券
     *
     * @param couponId 优惠券ID
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    void add(Long couponId);

    /**
     * 获取优惠券历史列表
     *
     * @param useStatus 优惠券筛选类型
     * @return 优惠券历史列表
     */
    List<SmsCouponHistory> listHistory(Integer useStatus);

    /**
     * 获取用户优惠券列表
     *
     * @param useStatus 优惠券筛选类型
     * @return 优惠券列表
     */
    List<SmsCoupon> list(Integer useStatus);

    /**
     * 获取当前商品相关优惠券
     *
     * @param productId 商品ID
     * @return 优惠券列表
     */
    List<SmsCoupon> listByProduct(Long productId);
}
