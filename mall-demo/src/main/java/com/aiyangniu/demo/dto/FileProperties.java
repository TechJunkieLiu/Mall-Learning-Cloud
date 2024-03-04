package com.aiyangniu.demo.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件上传下载自定义配置
 *
 * @author lzq
 * @date 2024/03/01
 */
@Data
@Component
@ConfigurationProperties(value = "file")
public class FileProperties {

    /**
     * 上传路径
     */
    private String uploadPath = "";

    /**
     * 下载路径
     */
    private String downloadPath = "";

    /**
     * 压缩路径
     */
    private String compressPath = "";

    /**
     * 解压路径
     */
    private String decompressPath = "";

    /**
     * 文件类型
     */
    private String[] fileTypeArray;

    /**
     * 文件大小
     */
    private int maxFileSize;
}
