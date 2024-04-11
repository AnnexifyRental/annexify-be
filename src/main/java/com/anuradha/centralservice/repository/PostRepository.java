package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByUuid(String uuid);

}
