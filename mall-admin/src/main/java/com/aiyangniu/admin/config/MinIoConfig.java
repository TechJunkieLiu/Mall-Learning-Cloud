package com.aiyangniu.admin.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO对象存储相关配置
 *
 * @author lzq
 * @date 2024/01/16
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinIoConfig {

    private String endPoint;
    private String bucketName;
    private String accessKey;
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        // 创建一个MinIO的Java客户端
        return MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
