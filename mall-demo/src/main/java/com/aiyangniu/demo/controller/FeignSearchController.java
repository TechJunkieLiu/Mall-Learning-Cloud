package com.aiyangniu.demo.controller;

import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.demo.service.FeignSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign调用mall-search接口示例
 *
 * @author lzq
 * @date 2023/09/26
 */
@Api(value = "FeignSearchController", tags = "Feign调用mall-search接口示例")
@RestController
@RequestMapping("/feign/search")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FeignSearchController {

    private final FeignSearchService feignSearchService;

    @ApiOperation(value = "简单商品搜索")
    @GetMapping(value = "/justSearch")
    public CommonResult search(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false, defaultValue = "0") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "5") Integer pageSize) {

        return feignSearchService.search(keyword, pageNum, pageSize);
    }
}
