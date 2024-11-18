package com.anuradha.centralservice.model;

import com.anuradha.centralservice.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @UuidGenerator
    private String id;
    private String title;
    private String description;
    private String address;
    private String rooms;
    private String beds;
    private String baths;
    private String size;
    private String price;
    @Column(columnDefinition = "TEXT")
    private String thumbnail;
    @Enumerated(EnumType.STRING)
    private CommonStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Post(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = CommonStatus.APPROVAL_PENDING;
    }

}
