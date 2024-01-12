package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.CmsPreferenceAreaService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.cms.CmsPreferenceArea;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品优选管理
 *
 * @author lzq
 * @date 2024/01/09
 */
@Api(value = "CmsPreferenceAreaController", tags = "商品优选管理")
@RestController
@RequestMapping("/preferenceArea")
@ApiSupport(author = "TechJunkieLiu")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CmsPreferenceAreaController {

    private final CmsPreferenceAreaService cmsPreferenceAreaService;

    @ApiOperation("获取所有商品优选")
    @GetMapping(value = "/listAll")
    public CommonResult<List<CmsPreferenceArea>> listAll() {
        List<CmsPreferenceArea> list = cmsPreferenceAreaService.listAll();
        return CommonResult.success(list);
    }

}
