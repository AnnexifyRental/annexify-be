package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import com.anuradha.centralservice.repository.PostImageRepository;
import com.anuradha.centralservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileUploaderService fileUploaderService;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository, FileUploaderService fileUploaderService) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.fileUploaderService = fileUploaderService;
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


    public void uploadImages(String id, MultipartFile thumbnail, List<MultipartFile> images) {
        validateUploadImagesRequest(thumbnail, images);

        Post post = postRepository.findById(id)
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
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getThumbnail(),
                postImageRepository.findByPost(post)
        );
    }

    private String uploadImage(MultipartFile image) {
//            return awsS3Client.uploadFile("central-images/" + image.getOriginalFilename(), image.getBytes(), image.getContentType());
        return fileUploaderService.uploadFile(image);
    }

    private void validateUploadImagesRequest(MultipartFile thumbnail, List<MultipartFile> images) {
        if (thumbnail == null || thumbnail.isEmpty() || thumbnail.getOriginalFilename() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thumbnail is required");

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
    public void delete(String id) {
        postImageRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }
}
