package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows", uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id","following_id"})})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="follower_id", nullable=false)
    private User follower;

    @ManyToOne @JoinColumn(name="following_id", nullable=false)
    private User following;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
