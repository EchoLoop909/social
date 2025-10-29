package com.example.social_network.models.Entity;

import com.example.social_network.models.Entity.enums.MediaVariant;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


import javax.persistence.*;

@Entity
@Table(name = "media")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="owner_user_id")
    private User owner;

    @ManyToOne @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne @JoinColumn(name="story_id")
    private Story story;

    @ManyToOne @JoinColumn(name="message_id")
    private Message message;

    @Column(columnDefinition = "TEXT", nullable=false)
    private String url;

    @Column(length=100)
    private String mimeType;

    private Integer width;
    private Integer height;
    private Long sizeBytes;
    private Integer durationSeconds;

    @Column(nullable=false)
    private Boolean isVideo = false;

    @Enumerated(EnumType.STRING)
    private MediaVariant variantKey;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
