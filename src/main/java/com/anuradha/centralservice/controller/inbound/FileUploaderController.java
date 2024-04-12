package com.anuradha.centralservice.controller.inbound;

import com.anuradha.centralservice.service.FileUploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
