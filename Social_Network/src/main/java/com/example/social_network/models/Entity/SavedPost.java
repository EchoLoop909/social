package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_posts")
@IdClass(SavedPostId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedPost {
    @Id
    private Long userId;

    @Id
    private Long postId;

    @CreationTimestamp
    private LocalDateTime savedAt;
}
