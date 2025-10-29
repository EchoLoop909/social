package com.example.social_network.Repository;

import com.example.social_network.models.Entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByUserId(@Param("id") Long id);

    Optional<User> findByUsername(String username);

}
