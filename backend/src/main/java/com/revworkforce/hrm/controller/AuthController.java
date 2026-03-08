package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.dto.AuthResponse;
import com.revworkforce.hrm.dto.LoginRequest;
import com.revworkforce.hrm.dto.PasswordResetRequest;
import com.revworkforce.hrm.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        authService.resetPassword(request);
    }
}
