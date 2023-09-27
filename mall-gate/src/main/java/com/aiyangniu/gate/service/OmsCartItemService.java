package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;

import java.util.List;

/**
 * 购物车管理接口
 *
 * @author lzq
 * @date 2023/09/26
 */
public interface OmsCartItemService {

    /**
     * 根据会员编号获取购物车列表
     *
     * @param memberId 会员编号
     * @return 购物车列表
     */
    List<OmsCartItem> list(Long memberId);
}
