package com.example.social_network.models.Entity;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTagId implements Serializable {
    private Long postId;
    private Long tagId;
}
