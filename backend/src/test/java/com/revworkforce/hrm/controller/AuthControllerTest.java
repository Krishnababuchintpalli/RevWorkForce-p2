package com.revworkforce.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revworkforce.hrm.dto.AuthResponse;
import com.revworkforce.hrm.dto.LoginRequest;
import com.revworkforce.hrm.dto.PasswordResetRequest;
import com.revworkforce.hrm.exception.GlobalExceptionHandler;
import com.revworkforce.hrm.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void loginShouldReturnToken() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("1001");
        request.setPassword("Employee@123");
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new AuthResponse("token-123", "EMPLOYEE", "Rahul Verma"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-123"))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    @Test
    public void resetPasswordShouldReturn200() throws Exception {
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmployeeId("1001");
        request.setEmail("rahul@rev.com");
        request.setNewPassword("Employee@123");
        doNothing().when(authService).resetPassword(any(PasswordResetRequest.class));

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
