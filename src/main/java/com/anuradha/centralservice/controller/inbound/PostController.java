package com.anuradha.centralservice.controller.inbound;


import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.PostImageSaveDto;
import com.anuradha.centralservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public IdResponseDto savePost(@RequestBody PostDto postDto) {
        return postService.savePost(postDto);
    }

    @PutMapping("images")
    public void savePostImages(@RequestBody PostImageSaveDto request) {
        postService.savePostImages(request);
    }

    @GetMapping
    public List<PostDto> findAll() {
        return postService.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam String id) {
        postService.delete(id);
    }


}
