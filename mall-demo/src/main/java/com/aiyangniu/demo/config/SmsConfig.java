package com.aiyangniu.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 短信验证码相关配置
 *
 * @author lzq
 * @date 2023/10/23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig {

    /** 试用授权码 **/
    private String appCode;
    /** 官方短信模板ID **/
    private String templateId;
}
