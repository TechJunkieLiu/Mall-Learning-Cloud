package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;

import java.util.List;

/**
 * 促销管理接口
 *
 * @author lzq
 * @date 2024/02/06
 */
public interface OmsPromotionService {

    /**
     * 计算购物车中的促销活动信息
     *
     * @param cartItemList 购物车
     * @return 促销活动信息
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
