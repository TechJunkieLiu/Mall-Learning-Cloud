package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.OmsOrderReturnApplyService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.bo.OmsOrderReturnApplyResult;
import com.aiyangniu.entity.model.dto.OmsReturnApplyQueryParamDTO;
import com.aiyangniu.entity.model.dto.OmsUpdateStatusParamDTO;
import com.aiyangniu.entity.model.pojo.oms.OmsOrderReturnApply;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单退货申请管理
 *
 * @author lzq
 * @date 2024/01/18
 */
@Api(value = "OmsOrderReturnApplyController", tags = "订单退货申请管理")
@RestController
@RequestMapping("/returnApply")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsOrderReturnApplyController {

    private final OmsOrderReturnApplyService omsOrderReturnApplyService;

    @ApiOperation("分页查询退货申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页条数", dataType = "Integer", defaultValue = "5"),
            @ApiImplicitParam(name = "pageNum", value = "当前页", dataType = "Integer", defaultValue = "1")
    })
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<OmsOrderReturnApply>> list(
            OmsReturnApplyQueryParamDTO queryParam,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1", required = false) Integer pageNum
    ) {
        List<OmsOrderReturnApply> returnApplyList = omsOrderReturnApplyService.list(queryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(returnApplyList));
    }

    @ApiOperation("批量删除退货申请")
    @PostMapping(value = "/delete")
    public CommonResult delete(
            @RequestParam(value = "ids", defaultValue = "") @ApiParam(name = "ids", value = "订单退货申请编号（多个用,分割）", example = "1,2,3", required = true) String ids
    ) {
        int count = omsOrderReturnApplyService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取退货申请详情")
    @GetMapping(value = "/{id}")
    public CommonResult getItem(@PathVariable Long id) {
        OmsOrderReturnApplyResult result = omsOrderReturnApplyService.getItem(id);
        return CommonResult.success(result);
    }

    @ApiOperation("修改退货申请状态")
    @PostMapping(value = "/update/status/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestBody OmsUpdateStatusParamDTO statusParam) {
        int count = omsOrderReturnApplyService.updateStatus(id, statusParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
