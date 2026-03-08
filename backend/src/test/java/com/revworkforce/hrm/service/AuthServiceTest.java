package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.LoginRequest;
import com.revworkforce.hrm.dto.AuthResponse;
import com.revworkforce.hrm.dto.PasswordResetRequest;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.UserRepository;
import com.revworkforce.hrm.security.JwtUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void loginShouldReturnTokenAndRoleWhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("1001");
        request.setPassword("Employee@123");

        UserDetails details = org.springframework.security.core.userdetails.User
                .withUsername("1001").password("x").authorities("ROLE_EMPLOYEE").build();
        User user = new User();
        user.setEmployeeId("1001");
        user.setFullName("Rahul Verma");
        user.setRole(Role.EMPLOYEE);
        user.setEmail("rahul@rev.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername("1001")).thenReturn(details);
        when(jwtUtil.generateToken(details)).thenReturn("jwt-token");
        when(userRepository.findByEmailOrEmployeeId("1001", "1001")).thenReturn(Optional.of(user));

        AuthResponse response = authService.login(request);
        Assert.assertEquals("jwt-token", response.getToken());
        Assert.assertEquals("EMPLOYEE", response.getRole());
    }

    @Test
    public void loginShouldThrowWhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("1001");
        request.setPassword("bad");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        Assert.assertThrows(UnauthorizedException.class, () -> authService.login(request));
    }

    @Test
    public void resetPasswordShouldEncodeAndPersistPassword() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmployeeId("1001");
        request.setEmail("rahul@rev.com");
        request.setNewPassword("Employee@123");

        User user = new User();
        user.setEmployeeId("1001");
        user.setEmail("rahul@rev.com");

        when(userRepository.findByEmployeeId("1001")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("Employee@123")).thenReturn("ENCODED");

        authService.resetPassword(request);

        verify(userRepository).save(user);
        Assert.assertEquals("ENCODED", user.getPassword());
    }

    @Test
    public void resetPasswordShouldThrowWhenEmailDoesNotMatch() {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmployeeId("1001");
        request.setEmail("wrong@rev.com");
        request.setNewPassword("Employee@123");

        User user = new User();
        user.setEmployeeId("1001");
        user.setEmail("rahul@rev.com");
        when(userRepository.findByEmployeeId(eq("1001"))).thenReturn(Optional.of(user));

        Assert.assertThrows(UnauthorizedException.class, () -> authService.resetPassword(request));
    }
}
