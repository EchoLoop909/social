package com.example.social_network.Controller;

import com.example.social_network.Payload.Util.PathResources;
import com.example.social_network.Service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(PathResources.FRIENTDSHIP)
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping(PathResources.SENDFRIENDREQUEST)
    public ResponseEntity<?> sendFriendRequest(@RequestParam Long sernderId,
                                               @RequestParam Long receiverId) {
        return friendshipService.sendFriendRequest(sernderId, receiverId);
    }

    @PostMapping(PathResources.ACCEPFRIENDREQUEST)
    public ResponseEntity<?> acceptFriendRequest(@RequestParam Long id) {
        return friendshipService.acceptFriendRequest(id);
    }

    // ✅ CHẶN NGƯỜI DÙNG
    @PostMapping(PathResources.BLOCKUSER)
    public ResponseEntity<?> blockUser(@RequestParam Long userId, @RequestParam Long targetId) {
        return friendshipService.blockUser(userId, targetId);
    }

    // ✅ THEO DÕI NGƯỜI DÙNG
    @PostMapping(PathResources.FLOWUSER)
    public ResponseEntity<?> followUser(@RequestParam Long userId, @RequestParam Long targetId) {
        return friendshipService.followUser(userId, targetId);
    }

    // ✅ HỦY KẾT BẠN
    @DeleteMapping(PathResources.UNFRIEND)
    public ResponseEntity<?> unfriend(@RequestParam Long userId, @RequestParam Long friendId) {
        return friendshipService.unfriendUser(userId, friendId);
    }
}
