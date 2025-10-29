package com.example.social_network.models;

import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String email;
    private String username;
    private String bio;

    private String password;
}