package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.enums.CommonStatus;
import com.anuradha.centralservice.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findAllByOrderByUpdatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.status IN ?1 AND p.createdAt BETWEEN ?2 AND ?3 ORDER BY p.updatedAt DESC")
    List<Post> findAll(List<CommonStatus> statuses, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
