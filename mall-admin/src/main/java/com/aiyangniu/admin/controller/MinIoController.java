package com.aiyangniu.admin.controller;

import com.aiyangniu.admin.config.MinIoConfig;
import com.aiyangniu.admin.utils.MinIoUtil;
import com.aiyangniu.common.api.CommonResult;
import com.aiyangniu.common.exception.ApiException;
import com.aiyangniu.entity.model.bo.MinIoUploadResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MinIO对象存储管理
 *
 * @author lzq
 * @date 2024/01/15
 */
@Slf4j
@Api(value = "MinIoController", tags = "MinIO 对象存储管理")
@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MinIoController {

    private final MinIoConfig minIoConfig;
    private final MinIoUtil minIoUtil;

    @ApiOperation("文件上传")
    @PostMapping(value = "/upload")
    public CommonResult upload(@RequestPart("file") MultipartFile file) {
        try {
            if (minIoUtil.bucketExists()) {
                log.info("存储桶已经存在！");
            } else {
                minIoUtil.makeBucket();
            }
            if (!StringUtils.hasLength(file.getOriginalFilename())) {
                throw new ApiException("文件夹名称不能为空！");
            }
            // 设置存储对象名称
            String objectName = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + file.getOriginalFilename();
            minIoUtil.createBucketFolder(objectName, file);
            log.info("文件上传成功!");
            MinIoUploadResult result = new MinIoUploadResult();
            result.setName(file.getOriginalFilename());
            result.setUrl(minIoConfig.getEndPoint() + "/" + minIoConfig.getBucketName() + "/" + objectName);
            return CommonResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("上传发生错误: {}！", e.getMessage());
        }
        return CommonResult.failed();
    }

    @ApiOperation("文件删除")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("objectName") String objectName) {
        try {
            minIoUtil.removeBucket(objectName);
            return CommonResult.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("删除发生错误：{}！", e.getMessage());
        }
        return CommonResult.failed();
    }
}
