package com.example.social_network.Service.ServiceImpl;

import com.example.social_network.Repository.FriendshipRepository;
import com.example.social_network.Repository.UserRepository;
import com.example.social_network.ResHelper.ResponseHelper;
import com.example.social_network.Service.FriendshipService;
import com.example.social_network.models.Entity.Friendship;
import com.example.social_network.models.Entity.User;
import com.example.social_network.models.Entity.enums.FriendshipStatus;
import com.example.social_network.models.dto.ResponseMess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private static final Logger logger = LoggerFactory.getLogger(FriendshipServiceImpl.class);

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> sendFriendRequest(Long senderId, Long receiverId) {
        try {
            User userSender = userRepository.findById(senderId)
                    .orElseThrow(() -> new Exception("User sender not found"));
            User userReceiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new Exception("User receiver not found"));

            Optional<Friendship> existing = friendshipRepository.findByUserAndFriend(userSender, userReceiver);

            if (existing.isPresent()) {
                Friendship friendship = existing.get();
                FriendshipStatus status = friendship.getStatus();

                switch (status) {
                    case BLOCKED:
                        throw new RuntimeException("Không thể gửi lời mời vì bạn đã bị chặn hoặc đã chặn người này.");
                    case PENDING:
                        throw new RuntimeException("Đã gửi lời mời kết bạn trước đó, đang chờ xác nhận.");
                    case ACCEPTED:
                        throw new RuntimeException("Hai bạn đã là bạn bè.");
                    case FOLLOWING:
                        friendship.setStatus(FriendshipStatus.PENDING);
                        friendship.setCreatedAt(LocalDateTime.now());
                        friendshipRepository.save(friendship);
                        logger.info("FOLLOWING → PENDING: {}", friendship.getId());
                        return ResponseHelper.getResponseSearchMess(
                                HttpStatus.OK,
                                new ResponseMess(0, "Đã gửi lời mời kết bạn (từ trạng thái theo dõi).")
                        );
                    default:
                        break;
                }
            }

            Friendship newFriendship = Friendship.builder()
                    .user(userSender)
                    .friend(userReceiver)
                    .status(FriendshipStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            friendshipRepository.save(newFriendship);

            logger.info("Friendship created successfully: {}", newFriendship.getId());
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.OK,
                    new ResponseMess(0, "Đã gửi lời mời kết bạn.")
            );

        } catch (Exception e) {
            logger.error("Error in sendFriendRequest: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, e.getMessage())
            );
        }
    }

    // ================== CHẤP NHẬN KẾT BẠN ==================
    @Override
    public ResponseEntity<?> acceptFriendRequest(Long id) {
        try {
            Friendship friendship = friendshipRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu kết bạn."));

            friendship.setStatus(FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendship);

            logger.info("Friend request accepted: {}", friendship.getId());
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.OK,
                    new ResponseMess(0, "Chấp nhận kết bạn thành công.")
            );

        } catch (Exception e) {
            logger.error("Error in acceptFriendRequest: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<?> followUser(Long followerId, Long followingId) {
        try {
            User follower = userRepository.findById(followerId)
                    .orElseThrow(() -> new Exception("Follower not found"));
            User following = userRepository.findById(followingId)
                    .orElseThrow(() -> new Exception("Following not found"));

            Optional<Friendship> existing = friendshipRepository.findByUserAndFriend(follower, following);

            if (existing.isPresent()) {
                Friendship friendship = existing.get();
                if (friendship.getStatus() == FriendshipStatus.BLOCKED)
                    throw new RuntimeException("Không thể theo dõi người bị chặn.");
                if (friendship.getStatus() == FriendshipStatus.FOLLOWING)
                    throw new RuntimeException("Bạn đã theo dõi người này.");

                friendship.setStatus(FriendshipStatus.FOLLOWING);
                friendshipRepository.save(friendship);
            } else {
                Friendship friendship = Friendship.builder()
                        .user(follower)
                        .friend(following)
                        .status(FriendshipStatus.FOLLOWING)
                        .createdAt(LocalDateTime.now())
                        .build();
                friendshipRepository.save(friendship);
            }

            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.OK,
                    new ResponseMess(0, "Theo dõi thành công.")
            );

        } catch (Exception e) {
            logger.error("Error in followUser: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<?> blockUser(Long userId, Long targetId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User not found"));
            User target = userRepository.findById(targetId)
                    .orElseThrow(() -> new Exception("Target user not found"));

            Optional<Friendship> existing = friendshipRepository.findByUserAndFriend(user, target);

            Friendship friendship = existing.orElseGet(() ->
                    Friendship.builder()
                            .user(user)
                            .friend(target)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

            friendship.setStatus(FriendshipStatus.BLOCKED);
            friendshipRepository.save(friendship);

            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.OK,
                    new ResponseMess(0, "Đã chặn người dùng.")
            );

        } catch (Exception e) {
            logger.error("Error in blockUser: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, e.getMessage())
            );
        }
    }

    @Override
    public ResponseEntity<?> unfriendUser(Long userId, Long friendId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new Exception("User not found"));
            User friend = userRepository.findById(friendId)
                    .orElseThrow(() -> new Exception("Friend not found"));

            Optional<Friendship> existing = friendshipRepository.findByUserAndFriend(user, friend);

            if (existing.isEmpty())
                throw new RuntimeException("Không tồn tại mối quan hệ để hủy.");

            Friendship friendship = existing.get();
            if (friendship.getStatus() != FriendshipStatus.ACCEPTED)
                throw new RuntimeException("Không thể hủy kết bạn vì chưa là bạn bè.");

            friendshipRepository.delete(friendship);

            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.OK,
                    new ResponseMess(0, "Đã hủy kết bạn thành công.")
            );

        } catch (Exception e) {
            logger.error("Error in unfriendUser: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, e.getMessage())
            );
        }
    }
}
