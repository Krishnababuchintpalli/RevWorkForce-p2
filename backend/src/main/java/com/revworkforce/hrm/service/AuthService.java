package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.AuthResponse;
import com.revworkforce.hrm.dto.LoginRequest;
import com.revworkforce.hrm.dto.PasswordResetRequest;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.repository.UserRepository;
import com.revworkforce.hrm.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails details = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(details);
            User user = userRepository.findByEmailOrEmployeeId(request.getUsername(), request.getUsername())
                    .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
            return new AuthResponse(token, user.getRole().name(), user.getFullName());
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByEmployeeId(request.getEmployeeId().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid employee details"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail().trim())) {
            throw new UnauthorizedException("Invalid employee details");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
