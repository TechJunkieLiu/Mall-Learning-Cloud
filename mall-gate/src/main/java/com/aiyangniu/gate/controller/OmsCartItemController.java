package com.aiyangniu.gate.controller;

import com.aiyangniu.common.api.CommonResult;
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
 * @date 2023/09/26
 */
@Api(value = "OmsCartItemController", tags = "购物车管理")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCartItemController {

    private final OmsCartItemService omsCartItemService;
    private final UmsMemberService umsMemberService;

    @ApiOperation("获取当前会员的购物车列表")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCartItem>> list() {
        List<OmsCartItem> cartItemList = omsCartItemService.list(umsMemberService.getCurrentMember().getId());
        return CommonResult.success(cartItemList);
    }
}
