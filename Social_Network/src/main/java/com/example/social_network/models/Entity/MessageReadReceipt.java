package com.example.social_network.models.Entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "message_read_receipts")
@IdClass(MessageReadReceiptId.class)
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageReadReceipt {
    @Id
    private Long messageId;

    @Id
    private Long userId;

    @CreationTimestamp
    private LocalDateTime readAt;
}
