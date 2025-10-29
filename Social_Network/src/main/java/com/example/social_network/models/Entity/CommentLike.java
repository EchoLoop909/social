package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"comment_id","user_id"})})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="comment_id", nullable=false)
    private Comment comment;

    @ManyToOne @JoinColumn(name="user_id", nullable=false)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_commentlike")
    private boolean isCommentlike;
}
