package com.anuradha.centralservice.controller.inbound;


import com.anuradha.centralservice.dto.FilterDto;
import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.PostImageSaveDto;
import com.anuradha.centralservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {


    private final PostService postService;

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

    @PostMapping("filter")
    public List<PostDto> findAll(@RequestBody FilterDto filterDto) {
        return postService.findAll(filterDto);
    }

    @GetMapping("by/id")
    public PostDto findById(@RequestParam String id) {
        return postService.findById(id);
    }

    @DeleteMapping
    public void delete(@RequestParam String id) {
        postService.delete(id);
    }


}
