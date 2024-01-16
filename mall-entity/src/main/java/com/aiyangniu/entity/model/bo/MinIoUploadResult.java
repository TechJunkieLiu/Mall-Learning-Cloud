package com.aiyangniu.entity.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MinIO对象存储文件上传返回结果
 *
 * @author lzq
 * @date 2023/09/20
 */
@Data
@EqualsAndHashCode
public class MinIoUploadResult {

    @ApiModelProperty("文件访问URL")
    private String url;

    @ApiModelProperty("文件名称")
    private String name;
}
