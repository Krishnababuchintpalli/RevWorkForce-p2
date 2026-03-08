package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.dto.AdminCreateUserRequest;
import com.revworkforce.hrm.dto.NotificationSendRequest;
import com.revworkforce.hrm.entity.Notification;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.service.NotificationService;
import com.revworkforce.hrm.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public User me() {
        return userService.me();
    }

    @PutMapping("/me")
    public User update(@RequestBody User request) {
        return userService.updateProfile(request);
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam String q) {
        return userService.search(q);
    }

    @GetMapping("/team")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<User> myTeam() {
        return userService.myTeam();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> all() {
        return userService.allUsers();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User create(@Valid @RequestBody AdminCreateUserRequest request) {
        return userService.createUser(request);
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public User setActive(@PathVariable Long id, @RequestParam boolean active) {
        return userService.activate(id, active);
    }

    @PatchMapping("/{id}/manager/{managerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public User assignManager(@PathVariable Long id, @PathVariable Long managerId) {
        return userService.assignManager(id, managerId);
    }

    @GetMapping("/notifications")
    public List<Notification> notifications() {
        return notificationService.myNotifications(userService.me().getId());
    }

    @PostMapping("/notifications/send")
    public void sendNotification(@Valid @RequestBody NotificationSendRequest request) {
        notificationService.sendNotification(request);
    }

    @PatchMapping("/notifications/{id}/read")
    public Notification updateReadStatus(@PathVariable Long id, @RequestParam boolean read) {
        return notificationService.updateReadStatus(id, read);
    }
}
