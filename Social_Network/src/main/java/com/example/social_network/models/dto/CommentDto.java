package com.example.social_network.models.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String content;
    private Long postId;
    private Long userId;
    private Long parentCommentId;
}
