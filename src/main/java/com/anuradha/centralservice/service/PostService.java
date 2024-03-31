package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
