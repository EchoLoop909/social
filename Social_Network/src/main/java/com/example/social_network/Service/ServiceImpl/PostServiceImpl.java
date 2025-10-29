package com.example.social_network.Service.ServiceImpl;

import com.example.social_network.Repository.LikeCommentRepository;
import com.example.social_network.models.Entity.PostLike;
import com.example.social_network.models.dto.PostDto;
import com.example.social_network.models.dto.ResponseMess;
import com.example.social_network.models.Entity.Post;
import com.example.social_network.models.Entity.User;
import com.example.social_network.Payload.Response.MessageResponse;
import com.example.social_network.Payload.Util.Status;
import com.example.social_network.Repository.PostLikeRepository;
import com.example.social_network.Repository.PostRepository;
import com.example.social_network.Repository.UserRepository;
import com.example.social_network.ResHelper.ResponseHelper;
import com.example.social_network.Service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private LikeCommentRepository likeCommentRepository;

    @Override
    public ResponseEntity<?> insert(PostDto dto) {
        try{
           Optional<User> userPost = userRepository.findById(dto.getUserId());
            if(userPost == null){
                return ResponseEntity.badRequest().body(new MessageResponse(Status.FAIL, "Tên đã tồn tại!", false, true));
            }

            Post post = new Post();
            post.setCaption(dto.getCaption());
            post.setCreatedAt(LocalDateTime.now());
            post.setLocation(dto.getLocation());
            post.setUpdatedAt(LocalDateTime.now());
            post.setUser(userPost.get());

            postRepository.save(post);
            logger.info("Post inserted successfully: {}",post.getId());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "INSERT SUCCESSFULLY"));

        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public Object getList(Long id, int pageIdx, int pageSize) {
        try{
            List<Post> results = new ArrayList<>();
            Pageable paging = PageRequest.of(pageIdx, pageSize);
            Page<Post> postPage;
            if (id == null) {
                postPage = postRepository.findAll(paging);
                results = postPage.getContent();
            }else {
                postPage = postRepository.findPost(id, paging);
                results = postPage.getContent();
                logger.info("Fetching post with provided search parameters.");
            }
            logger.info("get list post success page: " + pageIdx + " size: " + pageSize);
            return ResponseHelper.getResponses(results,postPage.getTotalElements(), postPage.getTotalPages(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            logger.info("Delete method Post start !");
            Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
            postRepository.delete(post);
            logger.info("Post deleted successfully with id: {}", id);
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "DELETE SUCCESSFULLY"));
        }catch (RuntimeException e) {
            logger.error("Error: " + e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.BAD_REQUEST, new ResponseMess(1, e.getMessage()));
        } catch (Exception e) {
            logger.error("error" + e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.EXPECTATION_FAILED, new ResponseMess(50, "Error excepted"));
        }
    }

    @Override
    public ResponseEntity<?> update(PostDto dto,Long id) {
        try {
            logger.info("Update method License start !");
            Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

            User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

            post.setCaption(dto.getCaption());
            post.setCreatedAt(LocalDateTime.now());
            post.setLocation(dto.getLocation());
            post.setUpdatedAt(LocalDateTime.now());
            post.setUser(user);

            postRepository.save(post);
            logger.info("insert success activity log");
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "UPDATE SUCCESSFULLY"));
        }catch (RuntimeException e) {
            logger.error("Error: " + e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.BAD_REQUEST, new ResponseMess(1, e.getMessage()));
        } catch (Exception e) {
            logger.error("error: " + e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50,"Error exception"));
        }
    }

    @Override
    public ResponseEntity<?> likePost(Long postId, Long userId) {
        try {
            Optional<PostLike> optionalPostLike = postLikeRepository.findByUserIdAndPostId(userId, postId);

            if (optionalPostLike.isPresent()) {
                // Đã tồn tại like rồi → toggle trạng thái
                PostLike existingLike = optionalPostLike.get();

                boolean currentStatus = existingLike.isPostlike();
                existingLike.setPostlike(!currentStatus); // đảo trạng thái like

                existingLike.setCreatedAt(LocalDateTime.now());
                postLikeRepository.save(existingLike);

                String message = currentStatus ? "UNLIKE SUCCESSFULLY" : "LIKE SUCCESSFULLY";
                return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, message));

            } else {
                // Chưa từng like → tạo mới
                Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new RuntimeException("Post not found"));

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                PostLike newLike = PostLike.builder()
                        .post(post)
                        .user(user)
                        .isPostlike(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                postLikeRepository.save(newLike);

                return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(0, "LIKE SUCCESSFULLY"));
            }

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseMess(50, "Error exception"));
        }
    }

    @Override
    public Object getListPostLike(Long id, int pageIdx, int pageSize) {
        try{
            List<PostLike> results = new ArrayList<>();
            Pageable paging = PageRequest.of(pageIdx, pageSize);
            Page<PostLike> postLikes;
            if (id == null) {
                postLikes = postLikeRepository.findAll(paging);
                results = postLikes.getContent();
            }else {
                postLikes = postLikeRepository.findPostLike(id, paging);
                results = postLikes.getContent();
                logger.info("Fetching post with provided search parameters.");
            }
            logger.info("get list post success page: " + pageIdx + " size: " + pageSize);
            return ResponseHelper.getResponses(results,postLikes.getTotalElements(), postLikes.getTotalPages(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
            return ResponseHelper.getResponseSearchMess(HttpStatus.INTERNAL_SERVER_ERROR, new ResponseMess(50, "Error exception"));
        }
    }

}
