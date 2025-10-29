package com.example.social_network.models.Entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedPostId implements Serializable {
    private Long userId;
    private Long postId;
}
