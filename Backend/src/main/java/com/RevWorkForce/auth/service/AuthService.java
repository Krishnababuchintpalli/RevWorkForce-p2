package com.RevWorkForce.auth.service;

import com.RevWorkForce.auth.dto.LoginRequest;
import com.RevWorkForce.auth.dto.LoginResponse;
import com.RevWorkForce.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
