package com.krutkowski.cars.model.dto;

public record AwsS3SaveImageModel(
        String awsUrl,
        String fileName
) {}
