package com.example.social_network.models.Entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageReadReceiptId implements Serializable {
    private Long messageId;
    private Long userId;
}
