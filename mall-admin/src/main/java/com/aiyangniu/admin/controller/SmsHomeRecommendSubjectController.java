package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.SmsHomeRecommendSubjectService;
import com.aiyangniu.common.api.CommonPage;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.entity.model.pojo.sms.SmsHomeRecommendSubject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页专题推荐管理
 *
 * @author lzq
 * @date 2024/03/15
 */
@Api(value = "SmsHomeRecommendSubjectController", tags = "首页专题推荐管理")
@RestController
@RequestMapping("/home/recommendSubject")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SmsHomeRecommendSubjectController {

    private final SmsHomeRecommendSubjectService recommendSubjectService;

    @ApiOperation("添加首页推荐专题")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody List<SmsHomeRecommendSubject> homeRecommendSubjectList) {
        int count = recommendSubjectService.create(homeRecommendSubjectList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping(value = "/update/sort/{id}")
    public CommonResult updateSort(@PathVariable Long id, Integer sort) {
        int count = recommendSubjectService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = recommendSubjectService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = recommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeRecommendSubject>> list(
            @RequestParam(value = "subjectName", required = false) String subjectName,
            @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
    ) {
        List<SmsHomeRecommendSubject> homeRecommendSubjectList = recommendSubjectService.list(subjectName, recommendStatus, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(homeRecommendSubjectList));
    }
}
