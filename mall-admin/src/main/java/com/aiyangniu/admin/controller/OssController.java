package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.service.OssService;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.entity.model.bo.OssCallbackResult;
import com.aiyangniu.entity.model.bo.OssPolicyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Oss对象存储管理
 *
 * @author lzq
 * @date 2024/01/19
 */
@Slf4j
@Api(value = "OssController", tags = "Oss 对象存储管理")
@RestController
@RequestMapping("/aliyun/oss")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OssController {

    private final OssService ossService;

    @ApiOperation(value = "Oss上传签名生成")
    @GetMapping(value = "/policy")
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

    @ApiOperation(value = "Oss上传成功回调")
    @PostMapping(value = "/callback")
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return CommonResult.success(ossCallbackResult);
    }

    @ApiOperation("Oss文件上传")
    @PostMapping("/upload")
    public CommonResult<String> upload(
            @ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file,
            @ApiParam(value = "模块", required = true) @RequestParam("module") String module
    ) {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = ossService.upload(inputStream, module, originalFilename);
            return CommonResult.success(uploadUrl, "文件上传成功！");
        } catch (IOException e) {
            log.error("文件上传错误：{}", ExceptionUtils.getStackTrace(e));
            throw new ApiException(e);
        }
    }

    @ApiOperation(value = "Oss文件删除")
    @DeleteMapping("/remove")
    public CommonResult<String> remove(@ApiParam(value = "要删除的文件路径", required = true) @RequestParam("url") String url) {
        ossService.remove(url);
        return CommonResult.success("文件删除成功！");
    }
}
