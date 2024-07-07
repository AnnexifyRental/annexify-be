package com.anuradha.centralservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class FileUploaderService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void uploadFile(MultipartFile file) {
        validateFile(file);

        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(uploadDir, filename);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload file : " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String contentType = file.getContentType();
        if (!Arrays.asList("image/jpeg", "image/png", "image/jpg").contains(contentType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid file type. Only PNG, JPEG, and JPG images are allowed.");
        }
    }

}
