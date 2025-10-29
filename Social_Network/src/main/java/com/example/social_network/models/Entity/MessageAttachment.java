package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_attachments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="message_id", nullable=false)
    private Message message;

    @Column(columnDefinition = "TEXT", nullable=false)
    private String url;

    @Column(length=100)
    private String mimeType;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
