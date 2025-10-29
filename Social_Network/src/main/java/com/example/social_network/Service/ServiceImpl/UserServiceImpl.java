package com.example.social_network.Service.ServiceImpl;

import com.example.social_network.models.CreateUserRequestDTO;
import com.example.social_network.models.dto.ResponseMess;
import com.example.social_network.models.Entity.User;
import com.example.social_network.models.Identity.*;
import com.example.social_network.models.LoginRequestDto;
import com.example.social_network.models.UserResponseDTO;
import com.example.social_network.Repository.IdentityClient;
import com.example.social_network.Repository.UserRepository;
import com.example.social_network.ResHelper.ResponseHelper;
import com.example.social_network.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IdentityClient identityClient;

    @Value("${keycloak.auth-server-url}")
    String serverUrl;

    @Value("${keycloak.realm}")
    String realm;

    @Value("${idp.client-id}")
    String clientId;

    @Value("${idp.client-secret}")
    String clientSecret;

    @Override
    public ResponseEntity<?> createUser(CreateUserRequestDTO req) {
        try{
            if (userRepository.findByEmail(req.getEmail()).isPresent()) {
                throw new RuntimeException("Email already exists: " + req.getEmail());
            }

            var token = identityClient.exchangeToken(TokenExchangeParam.builder()
                    .grant_type("client_credentials")
                    .client_id(clientId)
                    .client_secret(clientSecret)
                    .scope("openid")
                    .build());

            logger.info("TokenInfo {}", token);

            var creationResponse = identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(req.getUsername())
                            .email(req.getEmail())
                            .enabled(true)
                            .emailVerified(false)
                            .credentials(List.of(Credential.builder()
                                    .type("password")
                                    .temporary(false)
                                    .value(req.getPassword())
                                    .build()))
                            .build());

            String userId = extractUserId(creationResponse);
            logger.info("UserId {}", userId);

            User user = userRepository.save(toEntity(req, userId));
            return toDTO(user);
        }catch(Exception e){
            logger.error("error" + e.getMessage());
            return ResponseHelper.getResponseSearchMess(HttpStatus.OK, new ResponseMess(50, "Error excepted"));
        }
    }
    private String extractUserId(ResponseEntity<?> response) {
        List<String> locations = response.getHeaders().get("Location");
        if (locations == null || locations.isEmpty()) {
            throw new IllegalStateException("Location header is missing in the response");
        }
        String location = locations.get(0);
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }
    private User toEntity(CreateUserRequestDTO request, String id) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .bio(request.getBio())
                .isActive(Boolean.valueOf("true"))
                .isPrivate(Boolean.getBoolean("true"))
                .build();
    }

    private ResponseEntity<?> toDTO(User user) {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .build();

        return ResponseEntity.ok(dto);
    }

    @Override
    public TokenExchangeResponse login(LoginRequestDto dto) {
        var token = identityClient.exchangeUserToken(UserTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(clientId)
                .client_secret(clientSecret)
                .scope("openid")
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build());
        return token;
    }
}
