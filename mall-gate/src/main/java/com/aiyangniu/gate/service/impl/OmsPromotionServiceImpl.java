package com.aiyangniu.gate.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.bo.PromotionProduct;
import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;
import com.aiyangniu.entity.model.pojo.pms.PmsProductFullReduction;
import com.aiyangniu.entity.model.pojo.pms.PmsProductLadder;
import com.aiyangniu.entity.model.pojo.pms.PmsSkuStock;
import com.aiyangniu.gate.mapper.GateProductMapper;
import com.aiyangniu.gate.service.OmsPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 促销管理实现类
 *
 * @author lzq
 * @date 2024/02/06
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsPromotionServiceImpl implements OmsPromotionService {

    private final GateProductMapper gateProductMapper;

    @Override
    public List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList) {
        // TODO 1、先根据productId对CartItem进行分组，以spu为单位进行计算优惠
        Map<Long, List<OmsCartItem>> productCartMap = groupCartItemBySpu(cartItemList);
        // TODO 2、查询所有商品的优惠相关信息
        List<PromotionProduct> promotionProductList = getPromotionProductList(cartItemList);
        // TODO 3、根据商品促销类型计算商品促销优惠价格
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        for (Map.Entry<Long, List<OmsCartItem>> entry : productCartMap.entrySet()) {
            Long productId = entry.getKey();
            PromotionProduct promotionProduct = getPromotionProductById(productId, promotionProductList);
            List<OmsCartItem> itemList = entry.getValue();
            if (ObjectUtil.isNotEmpty(promotionProduct)){
                Integer promotionType = promotionProduct.getPromotionType();
                switch (promotionType){
                    // TODO 单品促销
                    case 1:
                        for (OmsCartItem item : itemList) {
                            CartPromotionItem cartPromotionItem = new CartPromotionItem();
                            BeanUtils.copyProperties(item, cartPromotionItem);
                            cartPromotionItem.setPromotionMessage("单品促销");
                            // 商品原价-促销价
                            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                            assert skuStock != null;
                            BigDecimal originalPrice = skuStock.getPrice();
                            // 单品促销使用原价
                            cartPromotionItem.setPrice(originalPrice);
                            cartPromotionItem.setReduceAmount(originalPrice.subtract(skuStock.getPromotionPrice()));
                            cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                            cartPromotionItemList.add(cartPromotionItem);
                        }
                        break;
                    // TODO 打折优惠
                    case 3:
                        int count = getCartItemCount(itemList);
                        PmsProductLadder ladder = getProductLadder(count, promotionProduct.getProductLadderList());
                        if(ObjectUtil.isNotEmpty(ladder)){
                            for (OmsCartItem item : itemList) {
                                CartPromotionItem cartPromotionItem = new CartPromotionItem();
                                BeanUtils.copyProperties(item, cartPromotionItem);
                                String message = getLadderPromotionMessage(ladder);
                                cartPromotionItem.setPromotionMessage(message);
                                // 商品原价-折扣*商品原价
                                PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                                assert skuStock != null;
                                BigDecimal originalPrice = skuStock.getPrice();
                                BigDecimal reduceAmount = originalPrice.subtract(ladder.getDiscount().multiply(originalPrice));
                                cartPromotionItem.setReduceAmount(reduceAmount);
                                cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                                cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                                cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                                cartPromotionItemList.add(cartPromotionItem);
                            }
                        }else{
                            handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                        }
                        break;
                    // TODO 满减
                    case 4:
                        BigDecimal totalAmount= getCartItemAmount(itemList, promotionProductList);
                        PmsProductFullReduction fullReduction = getProductFullReduction(totalAmount, promotionProduct.getProductFullReductionList());
                        if(ObjectUtil.isNotEmpty(fullReduction)){
                            for (OmsCartItem item : itemList) {
                                CartPromotionItem cartPromotionItem = new CartPromotionItem();
                                BeanUtils.copyProperties(item, cartPromotionItem);
                                assert fullReduction != null;
                                String message = getFullReductionPromotionMessage(fullReduction);
                                cartPromotionItem.setPromotionMessage(message);
                                // (商品原价/总价)*满减金额
                                PmsSkuStock skuStock= getOriginalPrice(promotionProduct, item.getProductSkuId());
                                BigDecimal originalPrice = skuStock.getPrice();
                                BigDecimal reduceAmount = originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getReducePrice());
                                cartPromotionItem.setReduceAmount(reduceAmount);
                                cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
                                cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
                                cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
                                cartPromotionItemList.add(cartPromotionItem);
                            }
                        }else{
                            handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                        }
                        break;
                    // TODO 无优惠
                    default:
                        handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                        break;
                }
            }
        }
        return cartPromotionItemList;
    }

    /**
     * 以spu为单位对购物车中商品进行分组
     */
    private Map<Long, List<OmsCartItem>> groupCartItemBySpu(List<OmsCartItem> cartItemList) {
        Map<Long, List<OmsCartItem>> productCartMap = new TreeMap<>();
        for (OmsCartItem cartItem : cartItemList) {
            List<OmsCartItem> productCartItemList = productCartMap.get(cartItem.getProductId());
            if (productCartItemList == null) {
                productCartItemList = new ArrayList<>();
                productCartItemList.add(cartItem);
                productCartMap.put(cartItem.getProductId(), productCartItemList);
            } else {
                productCartItemList.add(cartItem);
            }
        }
        return productCartMap;
    }

    /**
     * 查询所有商品的优惠相关信息
     */
    private List<PromotionProduct> getPromotionProductList(List<OmsCartItem> cartItemList) {
        List<Long> productIdList = new ArrayList<>();
        for(OmsCartItem cartItem : cartItemList){
            productIdList.add(cartItem.getProductId());
        }
        return gateProductMapper.getPromotionProductList(productIdList);
    }

    /**
     * 根据商品id获取商品的促销信息
     */
    private PromotionProduct getPromotionProductById(Long productId, List<PromotionProduct> promotionProductList) {
        for (PromotionProduct promotionProduct : promotionProductList) {
            if (productId.equals(promotionProduct.getId())) {
                return promotionProduct;
            }
        }
        return null;
    }

    /**
     * 获取商品的原价
     */
    private PmsSkuStock getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
        for (PmsSkuStock skuStock : promotionProduct.getSkuStockList()) {
            if (productSkuId.equals(skuStock.getId())) {
                return skuStock;
            }
        }
        return null;
    }

    /**
     * 获取购物车中指定商品的数量
     */
    private int getCartItemCount(List<OmsCartItem> itemList) {
        int count = 0;
        for (OmsCartItem item : itemList) {
            count += item.getQuantity();
        }
        return count;
    }

    /**
     * 根据购买商品数量获取满足条件的打折优惠策略
     */
    private PmsProductLadder getProductLadder(int count, List<PmsProductLadder> productLadderList) {
        // 按数量从大到小排序
        productLadderList.sort(new Comparator<PmsProductLadder>() {
            @Override
            public int compare(PmsProductLadder o1, PmsProductLadder o2) {
                return o2.getCount() - o1.getCount();
            }
        });
        for (PmsProductLadder productLadder : productLadderList) {
            if (count >= productLadder.getCount()) {
                return productLadder;
            }
        }
        return null;
    }

    /**
     * 获取打折优惠的促销信息
     */
    private String getLadderPromotionMessage(PmsProductLadder ladder) {
        StringBuilder sb = new StringBuilder();
        sb.append("打折优惠：").append("满").append(ladder.getCount()).append("件，").append("打").append(ladder.getDiscount().multiply(new BigDecimal(10))).append("折");
        return sb.toString();
    }

    /**
     * 对没满足优惠条件的商品进行处理
     */
    private void handleNoReduce(List<CartPromotionItem> cartPromotionItemList, List<OmsCartItem> itemList, PromotionProduct promotionProduct) {
        for (OmsCartItem item : itemList) {
            CartPromotionItem cartPromotionItem = new CartPromotionItem();
            BeanUtils.copyProperties(item, cartPromotionItem);
            cartPromotionItem.setPromotionMessage("无优惠");
            cartPromotionItem.setReduceAmount(new BigDecimal(0));
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
            if(skuStock != null){
                cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
            }
            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
            cartPromotionItemList.add(cartPromotionItem);
        }
    }

    /**
     * 获取购物车中指定商品的总价
     */
    private BigDecimal getCartItemAmount(List<OmsCartItem> itemList, List<PromotionProduct> promotionProductList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsCartItem item : itemList) {
            // 计算出商品原价
            PromotionProduct promotionProduct = getPromotionProductById(item.getProductId(), promotionProductList);
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
            amount = amount.add(skuStock.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return amount;
    }

    private PmsProductFullReduction getProductFullReduction(BigDecimal totalAmount, List<PmsProductFullReduction> fullReductionList) {
        // 按条件从高到低排序
        fullReductionList.sort(new Comparator<PmsProductFullReduction>() {
            @Override
            public int compare(PmsProductFullReduction o1, PmsProductFullReduction o2) {
                return o2.getFullPrice().subtract(o1.getFullPrice()).intValue();
            }
        });
        for(PmsProductFullReduction fullReduction:fullReductionList){
            if(totalAmount.subtract(fullReduction.getFullPrice()).intValue()>=0){
                return fullReduction;
            }
        }
        return null;
    }

    /**
     * 获取满减促销消息
     */
    private String getFullReductionPromotionMessage(PmsProductFullReduction fullReduction) {
        StringBuilder sb = new StringBuilder();
        sb.append("满减优惠：").append("满").append(fullReduction.getFullPrice()).append("元，").append("减").append(fullReduction.getReducePrice()).append("元");
        return sb.toString();
    }
}
