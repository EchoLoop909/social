package com.example.social_network.models.Entity;

import com.example.social_network.models.Entity.enums.NotificationType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // recipient
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    // actor who triggered the notification
    @ManyToOne @JoinColumn(name="actor_id")
    private User actor;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name="entity_type", length=50)
    private String entityType;

    @Column(name="entity_id")
    private Long entityId;

    @Column(nullable=false)
    private Boolean isRead = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
