package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.PostImageSaveDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import com.anuradha.centralservice.repository.PostImageRepository;
import com.anuradha.centralservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
    }


    public IdResponseDto savePost(PostDto postDto) {
        Post post = postRepository.save(new Post(
                postDto.title(),
                postDto.description()
        ));
        return new IdResponseDto(post.getId());
    }

    public List<PostDto> findAll() {
        return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(this::toPostDto)
                .toList();
    }

    @Transactional
    @Modifying
    public void delete(String id) {
        postImageRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }

    public void savePostImages(PostImageSaveDto request) {
        Post post = postRepository.findById(request.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post not found"));

        post.setThumbnail(request.thumbnail());
        postRepository.save(post);

        postImageRepository.deleteByPostId(request.id());
        if (request.images() == null || request.images().isEmpty()) return;
        postImageRepository.saveAll(
                request.images().stream()
                        .map(x -> new PostImage(post, x))
                        .toList()
        );
    }

    private PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getThumbnail(),
                postImageRepository.findByPost(post)
        );
    }
}
