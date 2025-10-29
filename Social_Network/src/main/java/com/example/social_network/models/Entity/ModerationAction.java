package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "moderation_actions")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ModerationAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="report_id")
    private Report report;

    @ManyToOne @JoinColumn(name="moderator_id")
    private User moderator;

    @Column(length=100)
    private String actionType;

    @Column(columnDefinition = "TEXT")
    private String actionDetails;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
