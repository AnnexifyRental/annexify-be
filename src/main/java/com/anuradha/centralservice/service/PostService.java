package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public void savePost(PostDto postDto) {
        postRepository.save(new Post(
                postDto.getTitle(),
                postDto.getDescription()
        ));
    }

    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(this::toPostDto)
                .toList();
    }

    private PostDto toPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setUuid(post.getUuid().toString());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());
        return postDto;
    }

}
