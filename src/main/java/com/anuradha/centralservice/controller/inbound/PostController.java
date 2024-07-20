package com.anuradha.centralservice.controller.inbound;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.UuidResponseDto;
import com.anuradha.centralservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public UuidResponseDto savePost(@RequestBody PostDto postDto) {
        return postService.savePost(postDto);
    }

    @PutMapping("images")
    public void uploadPostImages(@RequestParam String uuid, MultipartFile thumbnail, List<MultipartFile> images) {
        postService.uploadImages(uuid, thumbnail, images);
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam String uuid) {
        postService.delete(uuid);
    }


}
