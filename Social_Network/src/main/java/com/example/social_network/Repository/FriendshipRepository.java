package com.example.social_network.Repository;

import com.example.social_network.models.Entity.Friendship;
import com.example.social_network.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserAndFriend(User sernderId, User receiverId);
}
