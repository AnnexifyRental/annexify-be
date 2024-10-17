package com.anuradha.centralservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostImage {

    @Id
    @UuidGenerator
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;
    @Column(columnDefinition = "TEXT")
    private String url;

    public PostImage(Post post, String url) {
        this.post = post;
        this.url = url;
    }

}
