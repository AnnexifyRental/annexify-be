package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
