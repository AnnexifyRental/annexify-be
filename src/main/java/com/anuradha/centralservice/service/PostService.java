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


    public void uploadImages(UUID postUuid, MultipartFile thumbnail, List<MultipartFile> images) {
        validateUploadImagesRequest(thumbnail, images);

        Post post = postRepository.findByUuid(postUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post not found"));

        post.setThumbnail(uploadImage(thumbnail));
        postRepository.save(post);

        if (images == null || images.isEmpty()) return;
        postImageRepository.saveAll(
                images.stream()
                        .map(x -> new PostImage(post, uploadImage(x)))
                        .filter(x -> Objects.nonNull(x.getUrl()))
                        .toList()
        );

    }

    private PostDto toPostDto(Post post) {
        return new PostDto(
                post.getUuid().toString(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getThumbnail(),
                postImageRepository.findByPost(post)
        );
    }

    private String uploadImage(MultipartFile image) {
        try {
            return awsS3Client.uploadFile("central-images/" + image.getOriginalFilename(), image.getBytes(), image.getContentType());
        } catch (IOException e) {
            logger.error("PostService | uploadImage | Error: {}", e.getMessage());
            return null;
        }
    }

    private void validateUploadImagesRequest(MultipartFile thumbnail, List<MultipartFile> images) {
        if (thumbnail == null || thumbnail.isEmpty() || thumbnail.getOriginalFilename() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thumbnail is required");
//        if (images.isEmpty())
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Images are required");

        if (images != null && !images.isEmpty()) {
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

    }

    @Transactional
    @Modifying
    public void delete(UUID uuid) {
        postImageRepository.deleteByPostUuid(uuid);
        postRepository.deleteByUuid(uuid);
    }
}
