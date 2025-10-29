package com.example.social_network.Repository;

import com.example.social_network.models.Entity.PostLike;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserIdAndPostId(Long userId,Long postId);

    @Query("SELECT p FROM PostLike p WHERE (:id IS NULL OR p.id = :id)")
    Page<PostLike> findPostLike(@Param("id") Long id, Pageable pageable);
}
