package com.aiyangniu.gate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aiyangniu.entity.model.bo.CartProduct;
import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;
import com.aiyangniu.entity.model.pojo.ums.UmsMember;
import com.aiyangniu.gate.mapper.GateProductMapper;
import com.aiyangniu.gate.mapper.OmsCartItemMapper;
import com.aiyangniu.gate.service.OmsCartItemService;
import com.aiyangniu.gate.service.OmsPromotionService;
import com.aiyangniu.gate.service.UmsMemberService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车管理实现类
 *
 * @author lzq
 * @date 2024/02/06
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCartItemServiceImpl implements OmsCartItemService {

    private final OmsCartItemMapper omsCartItemMapper;
    private final OmsPromotionService omsPromotionService;
    private final UmsMemberService umsMemberService;
    private final GateProductMapper gateProductMapper;

    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember = umsMemberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem == null) {
            cartItem.setCreateDate(new Date());
            count = omsCartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyDate(new Date());
            existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            count = omsCartItemMapper.updateById(existCartItem);
        }
        return count;
    }

    /**
     * 根据会员id,商品id和规格获取购物车中商品
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        List<OmsCartItem> cartItemList = omsCartItemMapper.selectList(new LambdaQueryWrapper<OmsCartItem>()
                .eq(OmsCartItem::getMemberId, cartItem.getMemberId())
                .eq(OmsCartItem::getProductId, cartItem.getProductId())
                .eq(OmsCartItem::getDeleteStatus, 0)
                .eq(cartItem.getProductSkuId() != null, OmsCartItem::getProductSkuId, cartItem.getProductSkuId())
        );
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }
        return null;
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        return omsCartItemMapper.selectList(new LambdaQueryWrapper<OmsCartItem>().eq(OmsCartItem::getDeleteStatus, 0).eq(OmsCartItem::getMemberId, memberId));
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> omsCartItemList = omsCartItemMapper.selectList(new LambdaQueryWrapper<OmsCartItem>().eq(OmsCartItem::getDeleteStatus, 0).eq(OmsCartItem::getMemberId, memberId));
        if(CollUtil.isNotEmpty(cartIds)){
            omsCartItemList = omsCartItemList.stream().filter(item -> cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(omsCartItemList)){
            cartPromotionItemList = omsPromotionService.calcCartPromotion(omsCartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        return omsCartItemMapper.update(cartItem, new LambdaQueryWrapper<OmsCartItem>().eq(OmsCartItem::getDeleteStatus, 0).eq(OmsCartItem::getId, id).eq(OmsCartItem::getMemberId, memberId));
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        return omsCartItemMapper.update(record, new LambdaQueryWrapper<OmsCartItem>().in(OmsCartItem::getId, ids).eq(OmsCartItem::getMemberId, memberId));
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return gateProductMapper.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        // 删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyDate(new Date());
        updateCart.setDeleteStatus(1);
        omsCartItemMapper.updateById(updateCart);
        cartItem.setId(null);
        add(cartItem);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        return omsCartItemMapper.update(record, new LambdaQueryWrapper<OmsCartItem>().eq(OmsCartItem::getMemberId, memberId));
    }
}
