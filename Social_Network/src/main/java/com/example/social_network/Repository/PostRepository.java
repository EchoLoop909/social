package com.example.social_network.Repository;

import com.example.social_network.models.Entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
//    @Query(value = "SELECT * FROM posts " +
//            "WHERE  (:id IS NULL OR id = :id) " ,
//            nativeQuery = true)
//    Page<Post> findPost (@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE (:id IS NULL OR p.id = :id)")
    Page<Post> findPost(@Param("id") Long id, Pageable pageable);

    List<Post> id(Long id);

    @Query("SELECT p FROM Post p WHERE p.id = :id")
    Post findByPostId(@Param("id") Long id);
}
