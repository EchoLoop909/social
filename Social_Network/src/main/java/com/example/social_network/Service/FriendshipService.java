package com.example.social_network.Service;

import org.springframework.http.ResponseEntity;

public interface FriendshipService {
    ResponseEntity<?> sendFriendRequest(Long sernderId, Long receiverId);

    ResponseEntity<?> acceptFriendRequest(Long id);

    ResponseEntity<?> blockUser(Long userId, Long targetId);
    ResponseEntity<?> followUser(Long userId, Long targetId);
    ResponseEntity<?> unfriendUser(Long userId, Long friendId);
}
