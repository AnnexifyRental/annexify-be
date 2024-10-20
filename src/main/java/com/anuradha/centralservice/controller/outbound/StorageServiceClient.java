package com.anuradha.centralservice.controller.outbound;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

@Service
public class StorageServiceClient {

    private final RestTemplate restTemplate;

    public StorageServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String uploadImage(String id, String type, byte[] image) {
        return restTemplate.postForObject("http://localhost:8081/file-uploader/upload", image, String.class);
    }

}
