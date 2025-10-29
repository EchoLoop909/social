package com.example.social_network.Controller;

import com.example.social_network.Repository.UserRepository;
import com.example.social_network.models.CreateUserRequestDTO;
import com.example.social_network.models.Entity.User;
import com.example.social_network.models.Identity.TokenExchangeResponse;
import com.example.social_network.models.LoginRequestDto;
import com.example.social_network.Service.UserService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/public/api/register")
    public ResponseEntity<?> registerApi(@RequestBody CreateUserRequestDTO req) {
       return userService.createUser(req);
    }

    @PostMapping("/public/api/login")
    public ResponseEntity<TokenExchangeResponse> loginApi(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        // Lấy username từ token Keycloak
        String username = jwt.getClaimAsString("preferred_username");

        // Tìm user trong DB bằng username
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy user trong DB: " + username);
        }

        return ResponseEntity.ok(user.get());
    }
}