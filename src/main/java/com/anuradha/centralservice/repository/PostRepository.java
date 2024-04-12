package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByUuid(UUID uuid);

    List<Post> findAllByOrderByUpdatedAtDesc();
}
