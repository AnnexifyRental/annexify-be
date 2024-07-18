package com.anuradha.centralservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.s3.pre-signer.duration}")
    private int expirationTimeMinute;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public String createPutObjectPresignRequest(String fileName) {
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationTimeMinute))
                .putObjectRequest(x -> x.bucket(bucketName).key(fileName))
                .build();
        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(putObjectPresignRequest);
        return presigned.url().toString();
    }

    public String createGetObjectPresignRequest(String fileName) {
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expirationTimeMinute))
                .getObjectRequest(x -> x.bucket(bucketName).key(fileName))
                .build();
        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presigned.url().toString();
    }

}
