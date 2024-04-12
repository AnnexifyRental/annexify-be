package com.anuradha.centralservice.service;

import com.anuradha.centralservice.controller.outbound.AwsS3Client;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.UuidResponseDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import com.anuradha.centralservice.repository.PostImageRepository;
import com.anuradha.centralservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
public class PostService {

    // add logger
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private AwsS3Client awsS3Client;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostImageRepository postImageRepository;


    public UuidResponseDto savePost(PostDto postDto) {
        Post post = postRepository.save(new Post(
                postDto.getTitle(),
                postDto.getDescription()
        ));
        return new UuidResponseDto(post.getUuid().toString());
    }

    public List<PostDto> findAll() {
        return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
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
        postDto.setImages(postImageRepository.findByPost(post));
        return postDto;
    }

    public void uploadImages(UUID postUuid, List<MultipartFile> images) {
        validateUploadImagesRequest(images);

        Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post not found"));

        postImageRepository.saveAll(
                images.stream()
                        .map(image -> {
                            try {
                                String url = awsS3Client.uploadFile("central-images/" + image.getOriginalFilename(), image.getBytes(), image.getContentType());
                                return new PostImage(post, url);
                            } catch (IOException e) {
                                logger.error("PostService | uploadImages | postUuid: {} | Error: {}", postUuid, e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull) // Remove null entries if needed
                        .toList()
        );


    }

    private void validateUploadImagesRequest(List<MultipartFile> images) {
        if (images.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Images are required");
        if (images.size() > 5)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 5 images are allowed");
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty() || image.getOriginalFilename() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");


            String contentType = image.getContentType();
            if (!Arrays.asList("image/jpeg", "image/png", "image/jpg").contains(contentType)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid file type. Only PNG, JPEG, and JPG images are allowed.");
            }
        }

    }

    @Transactional
    @Modifying
    public void delete(UUID uuid) {
        postImageRepository.deleteByPostUuid(uuid);
        postRepository.deleteByUuid(uuid);
    }
}
