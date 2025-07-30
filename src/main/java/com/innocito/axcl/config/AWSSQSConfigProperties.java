package com.innocito.axcl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sentry.webhook.aws.sqs")
public class AWSSQSConfigProperties {
    private String accessKeyId;
    private String accessKeySecret;
    private String regionName;
    private String queueName;
    private String endpoint;
}