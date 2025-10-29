package com.example.social_network.Service;

import com.example.social_network.models.dto.PostDto;
import org.springframework.http.ResponseEntity;

public interface PostService {
    ResponseEntity<?> insert(PostDto dto);

    Object getList(Long id, int pageIdx, int pageSize);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<?> update(PostDto dto,Long id);

    ResponseEntity<?> likePost(Long postId, Long userId);

    Object getListPostLike(Long id, int i, int pageSize);
}
