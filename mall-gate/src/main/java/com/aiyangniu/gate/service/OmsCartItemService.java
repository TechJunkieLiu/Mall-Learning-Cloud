package com.aiyangniu.gate.service;

import com.aiyangniu.entity.model.bo.CartProduct;
import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车管理接口
 *
 * @author lzq
 * @date 2024/02/06
 */
public interface OmsCartItemService {

    /**
     * 查询购物车中是否包含该商品，有：增加数量，无：添加到购物车
     *
     * @param cartItem 购物车
     * @return 添加个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int add(OmsCartItem cartItem);

    /**
     * 根据会员编号获取购物车列表
     *
     * @param memberId 会员编号
     * @return 购物车列表
     */
    List<OmsCartItem> list(Long memberId);

    /**
     * 获取包含促销活动信息的购物车列表
     *
     * @param memberId 会员ID
     * @param cartIds 购物车ID
     * @return 购物车列表信息
     */
    List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds);

    /**
     * 修改某个购物车商品的数量
     *
     * @param id 购物车ID
     * @param memberId 会员ID
     * @param quantity 购买数量
     * @return 修改个数
     */
    int updateQuantity(Long id, Long memberId, Integer quantity);

    /**
     * 批量删除购物车中的商品
     *
     * @param memberId 会员ID
     * @param ids 购物车ID
     * @return 删除数量
     */
    int delete(Long memberId, List<Long> ids);

    /**
     * 获取购物车中用于选择商品规格的商品信息
     *
     * @param productId 商品ID
     * @return 购物车中带规格和SKU的商品信息
     */
    CartProduct getCartProduct(Long productId);

    /**
     * 修改购物车中商品的规格
     *
     * @param cartItem 购物车
     * @return 修改个数
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    int updateAttr(OmsCartItem cartItem);

    /**
     * 清空购物车
     *
     * @param memberId 会员ID
     * @return 清空个数
     */
    int clear(Long memberId);
}
