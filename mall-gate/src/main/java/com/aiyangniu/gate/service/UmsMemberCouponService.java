package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.bo.SmsCouponHistoryDetail;

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
}
