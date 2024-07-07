package com.anuradha.centralservice.controller.inbound;

import com.anuradha.centralservice.service.FileUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Deprecated
@RestController
@RequestMapping("file-uploader")
public class FileUploaderController {

    @Autowired
    private FileUploaderService fileUploaderService;

    @PostMapping()
    public void uploadImage(@RequestParam("image") MultipartFile file) {
        fileUploaderService.uploadFile(file);
    }


}
