package com.krutkowski.cars.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AwsS3Config {


    @Value("${amazon.aws.access-key-id}")
    private String awsS3AccessKey;

    @Value("${amazon.aws.access-key-secret}")
    private String awsS3SecretKey;

    @Value("${amazon.s3.region}")
    private String region;
    @Value("${amazon.s3.bucket}")
    String bucketName;

    @Bean
    public AmazonS3 amazonS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
