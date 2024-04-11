package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.UuidResponseDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FileUploaderService fileUploaderService;


    public UuidResponseDto savePost(PostDto postDto) {
        Post post = postRepository.save(new Post(
                postDto.getTitle(),
                postDto.getDescription()
        ));
        return new UuidResponseDto(post.getUuid().toString());
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

    public void uploadImages(String postUuid, List<MultipartFile> images) {
        validateUploadImagesRequest(images);

        postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post not found"));

        images.forEach(x -> fileUploaderService.uploadFile(x));

    }

    private void validateUploadImagesRequest(List<MultipartFile> images) {
        if (images.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Images are required");
        if (images.size() > 5)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 5 images are allowed");
    }
}
