package com.innocito.axcl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class AWSS3ConfigProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionName;
    private String bucketName;
    private long preSignedUrlExpiryInMillis;
}
