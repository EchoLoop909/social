package com.example.social_network.models.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "storie_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoryMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false, foreignKey = @ForeignKey(name = "fk_storymedia_story"))
    private Story story;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "is_video")
    private Boolean isVideo = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
