package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.CmsSubjectService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.cms.CmsSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品专题管理
 *
 * @author lzq
 * @date 2024/01/12
 */
@Api(value = "CmsSubjectController", tags = "商品专题管理")
@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CmsSubjectController {

    private final CmsSubjectService subjectService;

    @ApiOperation("获取全部商品专题")
    @GetMapping(value = "/listAll")
    public CommonResult<List<CmsSubject>> listAll() {
        List<CmsSubject> subjectList = subjectService.listAll();
        return CommonResult.success(subjectList);
    }

    @ApiOperation(value = "根据专题名称分页获取商品专题")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<CmsSubject>> getList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        List<CmsSubject> subjectList = subjectService.list(keyword, pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(subjectList));
    }
}
