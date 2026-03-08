package com.revworkforce.hrm.service;

import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrEmployeeId(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
