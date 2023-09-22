package com.aiyangniu.gateway.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 网关白名单配置
 *
 * @author lzq
 * @date 2023/09/22
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix="secure.ignore")
public class IgnoreUrlsConfig {

    /** 白名单 **/
    private List<String> urls;
}
