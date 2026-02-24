package com.RevWorkForce.auth.dto;

public class LoginRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // getters & setters
}