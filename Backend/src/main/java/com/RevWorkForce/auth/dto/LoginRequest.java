package com.revworkforce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @   Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // getters & setters

    public static class LoginResponse {

        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}