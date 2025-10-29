package com.example.social_network.Service.ServiceImpl;

import com.example.social_network.models.dto.CommentDto;
import com.example.social_network.models.dto.ResponseMess;
import com.example.social_network.models.Entity.Comment;
import com.example.social_network.models.Entity.CommentLike;
import com.example.social_network.models.Entity.Post;
import com.example.social_network.models.Entity.User;
import com.example.social_network.Repository.*;
import com.example.social_network.ResHelper.ResponseHelper;
import com.example.social_network.Service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    @Override
    public ResponseEntity<?> createComment(CommentDto dto) {
        try {
            Post post = postRepository.findByPostId(dto.getPostId());
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<User> userOpt = userRepository.findByUserId(dto.getUserId());
            if (userOpt == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User user = userOpt.get();

            Comment comment = new Comment();
            comment.setContent(dto.getContent());
            comment.setPost(post);
            comment.setUser(user);
            comment.setCreatedAt(LocalDateTime.now());
            comment.setIsDeleted(false);
            comment.setUpdatedAt(LocalDateTime.now());
            if (dto.getParentCommentId() != null) {
                Comment parent = commentRepository.findById(dto.getParentCommentId()).get();
                if (parent != null) {
                    comment.setParentComment(parent);
                }
            }

            commentRepository.save(comment);
            logger.info("Cooment inserted successfully: {}", post.getId());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "INSERT SUCCESSFULLY"));

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public Object getComments(Long postId) {
        try {
            Post post = postRepository.findByPostId(postId);
            if (post == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND
                );

            List<Comment> comments = commentRepository.findByPostIdAndIsDeletedFalse(postId);
            return new ResponseEntity<>(comments, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error fetching comments: {}", e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public ResponseEntity<?> updateComment(Long commentId, CommentDto dto) {
        try {
            Optional<Comment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND
                );

            Comment comment = commentOpt.get();
            comment.setContent(dto.getContent());
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "UPDATE SUCCESSFULLY"));

        } catch (Exception e) {
            logger.error("Error updating comment: {}", e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public ResponseEntity<?> deleteComment(Long commentId) {
        try {
            Optional<Comment> commentOpt = commentRepository.findById(commentId);
            if (commentOpt.isEmpty())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND
                );

            Comment comment = commentOpt.get();
            comment.setIsDeleted(true);
            comment.setUpdatedAt(LocalDateTime.now());
            commentRepository.save(comment);

            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "DELETE SUCCESSFULLY"));

        } catch (Exception e) {
            logger.error("Error deleting comment: {}", e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public ResponseEntity<?> likeComment(Long commentId, Long userId) {
        try {
            Optional<CommentLike> optionalLike = likeCommentRepository.findByCommentIdAndUserId(commentId, userId);

            if (optionalLike.isPresent()) {
                // Đã tồn tại -> toggle trạng thái
                CommentLike existingLike = optionalLike.get();

                boolean currentStatus = existingLike.isCommentlike();
                existingLike.setCommentlike(!currentStatus);
                existingLike.setCreatedAt(LocalDateTime.now());
                likeCommentRepository.save(existingLike);

                String message = currentStatus ? "UNLIKE SUCCESSFULLY" : "LIKE SUCCESSFULLY";
                return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, message));

            } else {
                // Chưa like -> tạo mới
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new RuntimeException("Comment not found"));

                CommentLike newLike = CommentLike.builder()
                        .user(user)
                        .comment(comment)
                        .isCommentlike(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                likeCommentRepository.save(newLike);
                return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "LIKE SUCCESSFULLY"));
            }

        } catch (Exception e) {
            logger.error("Error liking comment: {}", e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(HttpStatus.INTERNAL_SERVER_ERROR,
                    new ResponseMess(50, "Error occurred"));
        }
    }

}
