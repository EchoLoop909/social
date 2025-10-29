package com.example.social_network.Controller;

import com.example.social_network.models.dto.CommentDto;
import com.example.social_network.Payload.Util.PathResources;
import com.example.social_network.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathResources.COMMENT)
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping(PathResources.CREATECOMMENT)
    public ResponseEntity<?>createComment(@RequestBody CommentDto dto){
        return commentService.createComment(dto);
    }

    @GetMapping(PathResources.GETCOMMENT)
    public Object getComments(@RequestParam Long postId) {
        return commentService.getComments(postId);
    }

    @PutMapping(PathResources.UPDATECOMMENT)
    public ResponseEntity<?> updateComment(@RequestParam Long commentId, @RequestBody CommentDto dto) {
        return commentService.updateComment(commentId, dto);
    }

    @DeleteMapping(PathResources.DELETECOMMENT)
    public ResponseEntity<?> deleteComment(@RequestParam Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @PostMapping(PathResources.LIKECOMMENT)
    public ResponseEntity<?> likeComment(@RequestParam Long commentId,
                                         @RequestParam Long userId) {
        return commentService.likeComment(commentId,userId);
    }
}
