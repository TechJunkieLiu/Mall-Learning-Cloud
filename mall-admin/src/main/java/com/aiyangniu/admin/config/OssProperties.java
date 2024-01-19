package com.aiyangniu.admin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OSS对象存储相关配置属性类（读取YML配置文件）
 *
 * @author lzq
 * @date 2024/01/19
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties implements InitializingBean {

    private String endPoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String dirPrefix;
    private Integer policyExpire;
    private Integer maxSize;
    private String callback;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    public static String DIR_PREFIX;
    public static Integer POLICY_EXPIRE;
    public static Integer MAX_SIZE;
    public static String CALLBACK;

    /**
     * 当私有成员被赋值后，此方法自动被调用，从而初始化常量
     */
    @Override
    public void afterPropertiesSet() {
        END_POINT = endPoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
        DIR_PREFIX = dirPrefix;
        POLICY_EXPIRE = policyExpire;
        MAX_SIZE = maxSize;
        CALLBACK = callback;
    }
}
