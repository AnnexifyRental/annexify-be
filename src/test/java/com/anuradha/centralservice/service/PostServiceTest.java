package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.IdResponseDto;
import com.anuradha.centralservice.dto.PostDto;
import com.anuradha.centralservice.dto.PostImageSaveDto;
import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import com.anuradha.centralservice.repository.PostImageRepository;
import com.anuradha.centralservice.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PostServiceTest {


    @Mock
    private PostRepository postRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void savePost() {
        PostDto postDto = new PostDto("1", "Title", "Description", null, null, null, null);
        Post post = new Post("Title", "Description");
        post.setId("1");

        when(postRepository.save(any(Post.class))).thenReturn(post);

        IdResponseDto response = postService.savePost(postDto);

        assertNotNull(response);
        assertEquals("1", response.id());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void findAll() {
        Post post = new Post("Title", "Description");
        post.setId("1");

        when(postRepository.findAllByOrderByUpdatedAtDesc()).thenReturn(List.of(post));
        when(postImageRepository.findByPost(post)).thenReturn(List.of());

        List<PostDto> posts = postService.findAll();

        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Title", posts.get(0).title());
        verify(postRepository, times(1)).findAllByOrderByUpdatedAtDesc();
    }

    @Test
    void delete() {
        doNothing().when(postImageRepository).deleteByPostId("1");
        doNothing().when(postRepository).deleteById("1");

        postService.delete("1");

        verify(postImageRepository, times(1)).deleteByPostId("1");
        verify(postRepository, times(1)).deleteById("1");
    }

    @Test
    void savePostImages() {
        PostImageSaveDto request = new PostImageSaveDto("1", "thumbnail.jpg", List.of("image1.jpg", "image2.jpg"));
        Post post = new Post("Title", "Description");
        post.setId("1");

        when(postRepository.findById("1")).thenReturn(Optional.of(post));
        when(postImageRepository.saveAll(anyList())).thenReturn(List.of(new PostImage(post, "image1.jpg"), new PostImage(post, "image2.jpg")));

        postService.savePostImages(request);

        verify(postRepository, times(1)).findById("1");
        verify(postRepository, times(1)).save(post);
        verify(postImageRepository, times(1)).deleteByPostId("1");
        verify(postImageRepository, times(1)).saveAll(anyList());
    }

    @Test
    void savePostImages_PostNotFound() {
        PostImageSaveDto request = new PostImageSaveDto("1", "thumbnail.jpg", List.of("image1.jpg", "image2.jpg"));

        when(postRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> postService.savePostImages(request));

        verify(postRepository, times(1)).findById("1");
        verify(postRepository, never()).save(any(Post.class));
        verify(postImageRepository, never()).deleteByPostId(anyString());
        verify(postImageRepository, never()).saveAll(anyList());
    }
}