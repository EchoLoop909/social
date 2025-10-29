package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_likes",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id","user_id"})})
@Data @NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @ManyToOne @JoinColumn(name="user_id", nullable=false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_postlike")
    private boolean isPostlike;
}
