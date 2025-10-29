package com.example.social_network.Service;

import com.example.social_network.models.CreateUserRequestDTO;
import com.example.social_network.models.Identity.TokenExchangeResponse;
import com.example.social_network.models.LoginRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(CreateUserRequestDTO req);

    TokenExchangeResponse login(LoginRequestDto dto);

}
