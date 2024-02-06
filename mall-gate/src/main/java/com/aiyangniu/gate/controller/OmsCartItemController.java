package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.CartProduct;
import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.pojo.oms.OmsCartItem;
import com.aiyangniu.gate.service.OmsCartItemService;
import com.aiyangniu.gate.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 *
 * @author lzq
 * @date 2024/02/06
 */
@Api(value = "OmsCartItemController", tags = "购物车管理")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCartItemController {

    private final OmsCartItemService omsCartItemService;
    private final UmsMemberService umsMemberService;

    @ApiOperation("添加商品到购物车")
    @PostMapping(value = "/add")
    public CommonResult add(@RequestBody OmsCartItem cartItem) {
        int count = omsCartItemService.add(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取当前会员的购物车列表")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCartItem>> list() {
        List<OmsCartItem> cartItemList = omsCartItemService.list(1L);
        return CommonResult.success(cartItemList);
    }

    @ApiOperation("获取当前会员的购物车列表(包括促销信息)")
    @GetMapping(value = "/list/promotion")
    public CommonResult<List<CartPromotionItem>> listPromotion(@RequestParam(required = false) List<Long> cartIds) {
        List<CartPromotionItem> cartPromotionItemList = omsCartItemService.listPromotion(1L, cartIds);
        return CommonResult.success(cartPromotionItemList);
    }

    @ApiOperation("修改购物车中指定商品的数量")
    @GetMapping(value = "/update/quantity")
    public CommonResult updateQuantity(@RequestParam Long id, @RequestParam Integer quantity) {
        int count = omsCartItemService.updateQuantity(id, 1L, quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取购物车中指定商品的规格(用于重选规格)")
    @GetMapping(value = "/getProduct/{productId}")
    public CommonResult<CartProduct> getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = omsCartItemService.getCartProduct(productId);
        return CommonResult.success(cartProduct);
    }

    @ApiOperation("修改购物车中商品的规格")
    @PostMapping(value = "/update/attr")
    public CommonResult updateAttr(@RequestBody OmsCartItem cartItem) {
        int count = omsCartItemService.updateAttr(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除购物车中的指定商品")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = omsCartItemService.delete(umsMemberService.getCurrentMember().getId(), ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("清空当前会员的购物车")
    @PostMapping(value = "/clear")
    public CommonResult clear() {
        int count = omsCartItemService.clear(umsMemberService.getCurrentMember().getId());
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
