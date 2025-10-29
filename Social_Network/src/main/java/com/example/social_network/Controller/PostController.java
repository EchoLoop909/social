package com.example.social_network.Controller;

import com.example.social_network.models.dto.PostDto;
import com.example.social_network.Payload.Util.PathResources;
import com.example.social_network.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathResources.Post)
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping(PathResources.INSERT)
    public ResponseEntity<?> insert(@RequestBody PostDto dto){
        return postService.insert(dto);
    }

    @GetMapping(PathResources.GETPOST)
    public Object getList(@RequestParam(required = false) Long id,
                                     @RequestParam(defaultValue = "1") int pageIdx,
                                     @RequestParam(defaultValue = "100") int pageSize){
        return postService.getList(id,pageIdx -1,pageSize);
    }

    @DeleteMapping(PathResources.DELETE)
    public ResponseEntity<?> delete(@RequestParam Long id){
        return postService.delete(id);
    }

    @PutMapping(PathResources.UPDATE)
    public ResponseEntity<?> update(@RequestBody PostDto dto,
                                    @RequestParam Long id){
        return postService.update(dto,id);
    }

    @PostMapping(PathResources.POSTLIKE)
    public ResponseEntity<?>likePost(@RequestParam Long postId,
                                     @RequestParam Long userId){
        return postService.likePost(postId,userId);
    }

    @GetMapping(PathResources.GETPOSTLIKE)
    public Object getListPostLike(@RequestParam(required = false) Long id,
                          @RequestParam(defaultValue = "1") int pageIdx,
                          @RequestParam(defaultValue = "100") int pageSize){
        return postService.getListPostLike(id,pageIdx -1,pageSize);
    }
}
