package com.innocito.axcl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
/*import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;*/

@RequiredArgsConstructor
@Configuration
public class AWSConfiguration {
    //private final AWSS3ConfigProperties awss3ConfigProperties;
    private AWSSQSConfigProperties awssqsConfigProperties;

    /*@Bean
    public S3Client getS3Client() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                awss3ConfigProperties.getAccessKeyId(),
                awss3ConfigProperties.getAccessKeySecret()
        );
        return S3Client.builder()
                .region(Region.of(awss3ConfigProperties.getRegionName()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                awss3ConfigProperties.getAccessKeyId(),
                awss3ConfigProperties.getAccessKeySecret()
        );

        return S3Presigner.builder()
                .region(Region.of(awss3ConfigProperties.getRegionName()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }*/

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.of(awssqsConfigProperties.getRegionName()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                awssqsConfigProperties.getAccessKeyId(),
                                awssqsConfigProperties.getAccessKeySecret()
                        )
                ))
                .build();
    }
}
