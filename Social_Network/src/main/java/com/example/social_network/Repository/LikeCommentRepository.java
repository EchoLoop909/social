package com.example.social_network.Repository;

import com.example.social_network.models.Entity.CommentLike;
import com.example.social_network.models.Entity.Post;
import com.example.social_network.models.Entity.PostLike;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

}
