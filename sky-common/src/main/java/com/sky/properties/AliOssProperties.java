package com.sky.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
//封装配置项的值
@ConfigurationProperties(prefix = "aliyun.oss")

public class AliOssProperties {
    private String endpoint;
    private String bucketName;
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
}
