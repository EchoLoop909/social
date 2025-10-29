package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_members")
@IdClass(ConversationMemberId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMember {
    @Id
    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Id
    private Long userId;

    @CreationTimestamp
    private LocalDateTime joinedAt;
}
