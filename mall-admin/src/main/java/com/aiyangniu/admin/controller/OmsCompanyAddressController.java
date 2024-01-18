package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.OmsCompanyAddressService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.oms.OmsCompanyAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 收货地址管理
 *
 * @author lzq
 * @date 2024/01/16
 */
@Api(value = "OmsCompanyAddressController", tags = "收货地址管理")
@RestController
@RequestMapping("/companyAddress")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OmsCompanyAddressController {

    private final OmsCompanyAddressService companyAddressService;

    @ApiOperation("获取所有收货地址")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
