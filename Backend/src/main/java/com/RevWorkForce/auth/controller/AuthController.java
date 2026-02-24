package com.RevWorkForce.auth.controller;

import com.RevWorkForce.auth.dto.LoginRequest;
import com.RevWorkForce.auth.dto.LoginResponse;
import com.RevWorkForce.auth.dto.RegisterRequest;
import com.RevWorkForce.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/employee/test")
    public String test() {
        return "Employee endpoint working";
    }
}
