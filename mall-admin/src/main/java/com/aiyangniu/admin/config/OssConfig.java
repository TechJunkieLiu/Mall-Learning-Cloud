package com.aiyangniu.admin.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS对象存储相关配置
 *
 * @author lzq
 * @date 2023/09/28
 */
@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint}")
    private String aLiYunOssEndPoint;
    
    @Value("${aliyun.oss.accessKeyId}")
    private String aLiYunOssAccessKeyId;
    
    @Value("${aliyun.oss.accessKeySecret}")
    private String aLiYunOssAccessKeySecret;
    
    @Bean
    public OSSClient ossClient(){
        return new OSSClient(aLiYunOssEndPoint, aLiYunOssAccessKeyId, aLiYunOssAccessKeySecret);
    }
}
