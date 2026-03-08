package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.AdminCreateUserRequest;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CurrentUserService currentUserService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
    }

    public User me() {
        return currentUserService.getCurrentUser();
    }

    @Transactional
    public User updateProfile(User request) {
        User current = currentUserService.getCurrentUser();
        current.setPhone(request.getPhone());
        current.setAddress(request.getAddress());
        current.setEmergencyContact(request.getEmergencyContact());
        return userRepository.save(current);
    }

    public List<User> search(String query) {
        return userRepository.searchByNameOrEmployeeId(query);
    }

    public List<User> myTeam() {
        return userRepository.findByManagerId(currentUserService.getCurrentUser().getId());
    }

    @Transactional
    public User assignManager(Long employeeId, Long managerId) {
        User emp = userRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        User mgr = userRepository.findById(managerId).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        emp.setManager(mgr);
        return userRepository.save(emp);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User activate(Long id, boolean active) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(active);
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(AdminCreateUserRequest request) {
        try {
            Long.parseLong(request.getEmployeeId().trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Employee ID must be numeric to match database schema");
        }

        if (userRepository.findByEmployeeId(request.getEmployeeId()).isPresent()) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmployeeId(request.getEmployeeId().trim());
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);

        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
            user.setManager(manager);
        }
        return userRepository.save(user);
    }
}
