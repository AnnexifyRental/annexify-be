package com.anuradha.centralservice.controller;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin(origins = "http://localhost:19000")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public void savePost(@RequestBody PostDto postDto) {
        postService.savePost(postDto);
    }

    @PutMapping("images")
    public void uploadPostImages(@RequestParam String postUuid, @RequestParam("image") List<MultipartFile> images) {
        postService.uploadImages(postUuid, images);
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
    }

}
