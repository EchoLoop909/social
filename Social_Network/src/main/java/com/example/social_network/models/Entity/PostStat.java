package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_stats")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostStat {
    @Id
    private Long postId;

    private Long likeCount = 0L;
    private Long commentCount = 0L;
    private Long viewCount = 0L;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
