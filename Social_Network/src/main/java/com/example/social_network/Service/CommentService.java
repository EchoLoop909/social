package com.example.social_network.Service;

import com.example.social_network.models.dto.CommentDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> createComment(CommentDto dto);

    Object getComments(Long postId);

    ResponseEntity<?> updateComment(Long commentId, CommentDto dto);

    ResponseEntity<?> deleteComment(Long commentId);

    ResponseEntity<?> likeComment(Long commentId, Long userId);
}
