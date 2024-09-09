package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.CartPromotionItem;
import com.aiyangniu.entity.model.bo.SmsCouponHistoryDetail;
import com.aiyangniu.entity.model.pojo.sms.SmsCoupon;
import com.aiyangniu.entity.model.pojo.sms.SmsCouponHistory;
import com.aiyangniu.gate.service.OmsCartItemService;
import com.aiyangniu.gate.service.UmsMemberCouponService;
import com.aiyangniu.gate.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员优惠券管理
 *
 * @author lzq
 * @date 2024/09/09
 */
@Api(value = "UmsMemberCouponController", tags = "会员优惠券管理")
@RestController
@RequestMapping("/member/coupon")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UmsMemberCouponController {

    private final UmsMemberCouponService umsMemberCouponService;
    private final OmsCartItemService omsCartItemService;
    private final UmsMemberService umsMemberService;

    @ApiOperation("领取指定优惠券")
    @PostMapping(value = "/add/{couponId}")
    public CommonResult add(@PathVariable Long couponId) {
        umsMemberCouponService.add(couponId);
        return CommonResult.success(null,"领取成功");
    }

    @ApiOperation("获取会员优惠券历史列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期", allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @GetMapping(value = "/listHistory")
    public CommonResult<List<SmsCouponHistory>> listHistory(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        List<SmsCouponHistory> couponHistoryList = umsMemberCouponService.listHistory(useStatus);
        return CommonResult.success(couponHistoryList);
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期", allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @GetMapping(value = "/list")
    public CommonResult<List<SmsCoupon>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus){
        List<SmsCoupon> couponList = umsMemberCouponService.list(useStatus);
        return CommonResult.success(couponList);
    }

    @ApiOperation("获取登录会员购物车的相关优惠券")
    @ApiImplicitParam(name = "type", value = "使用可用:0->不可用；1->可用", defaultValue = "1", allowableValues = "0,1", paramType = "path", dataType = "integer")
    @GetMapping(value = "/list/cart/{type}")
    public CommonResult<List<SmsCouponHistoryDetail>> listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = omsCartItemService.listPromotion(umsMemberService.getCurrentMember().getId(), null);
        List<SmsCouponHistoryDetail> couponHistoryList = umsMemberCouponService.listCart(cartPromotionItemList, type);
        return CommonResult.success(couponHistoryList);
    }

    @ApiOperation("获取当前商品相关优惠券")
    @GetMapping(value = "/listByProduct/{productId}")
    public CommonResult<List<SmsCoupon>> listByProduct(@PathVariable Long productId) {
        List<SmsCoupon> couponHistoryList = umsMemberCouponService.listByProduct(productId);
        return CommonResult.success(couponHistoryList);
    }
}
