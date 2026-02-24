package com.revworkforce.auth.service;

import com.revworkforce.auth.dto.LoginRequest;
import com.revworkforce.auth.dto.LoginResponse;
import com.revworkforce.auth.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
