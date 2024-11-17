package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.FilterDto;
import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.PostImageSaveDto;
import com.anuradha.centralservice.enums.CommonStatus;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import com.anuradha.centralservice.repository.PostImageRepository;
import com.anuradha.centralservice.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

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

    public List<PostDto> findAll(FilterDto filter) {
        validateFindAllFilter(filter);

        int page = filter.getPage();
        int size = filter.getSize();
        List<CommonStatus> statuses = filter.getStatuses();
        LocalDateTime fromDate = filter.getFromDate().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime toDate = filter.getToDate().withHour(23).withMinute(59).withSecond(59);


        if (statuses == null || statuses.isEmpty())
            return postRepository.findAll(statuses, fromDate, toDate, PageRequest.of(page, size))
                    .stream()
                    .map(this::toPostDto)
                    .toList();


        return postRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(this::toPostDto)
                .toList();
    }

    private void validateFindAllFilter(FilterDto filter) {
        if (filter == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filter data is required");
        if (filter.getFromDate() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "From date is required");
        if (filter.getToDate() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "To date is required");

        if (filter.getFromDate().isAfter(filter.getToDate()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range");

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

    public PostDto findById(String id) {
        return postRepository.findById(id)
                .map(this::toPostDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post not found"));
    }
}
