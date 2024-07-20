package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.model.Post;
import com.anuradha.centralservice.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PostImageRepository extends JpaRepository<PostImage, Integer> {

    @Query("SELECT p.url FROM PostImage p WHERE p.post = ?1")
    List<String> findByPost(Post post);

    void deleteByPostUuid(String uuid);
}
