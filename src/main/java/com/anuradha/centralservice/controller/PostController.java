package com.anuradha.centralservice.controller;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public void savePost(@RequestBody PostDto postDto) {
        postService.savePost(postDto);
    }

}
