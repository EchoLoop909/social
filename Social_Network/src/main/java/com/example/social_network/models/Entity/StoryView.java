package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "story_views")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@IdClass(StoryViewId.class)
public class StoryView {
    @Id
    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Id
    @ManyToOne
    @JoinColumn(name = "viewer_id", nullable = false)
    private User viewer;

    @CreationTimestamp
    private LocalDateTime viewedAt;
}
