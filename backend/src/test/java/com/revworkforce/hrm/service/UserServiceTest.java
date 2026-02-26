package com.revworkforce.hrm.service;

import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnCurrentUserProfile() {
        User user = new User();
        user.setEmail("employee@revworkforce.com");
        user.setFullName("Sample Employee");
        user.setRole(Role.EMPLOYEE);

        when(currentUserService.getCurrentUser()).thenReturn(user);

        User result = userService.me();
        Assert.assertEquals("Sample Employee", result.getFullName());
    }
}
