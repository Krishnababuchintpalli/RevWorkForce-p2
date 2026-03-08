package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.NotificationSendRequest;
import com.revworkforce.hrm.entity.Notification;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.NotificationRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository,
                               CurrentUserService currentUserService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void notify(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.saveAndFlush(notification);
    }

    public List<Notification> myNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByIdDesc(userId);
    }

    @Transactional
    public void sendNotification(NotificationSendRequest request) {
        User sender = currentUserService.getCurrentUser();
        String message = request.getMessage().trim();
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        if (sender.getRole() == Role.ADMIN) {
            if (request.isSendToAll()) {
                userRepository.findAll().stream()
                        .filter(User::isActive)
                        .forEach(u -> notify(u, message));
                return;
            }
            if (request.getRecipientUserId() == null) {
                throw new IllegalArgumentException("Recipient is required when send-to-all is not selected");
            }
            User recipient = userRepository.findById(request.getRecipientUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
            if (recipient.getRole() != Role.EMPLOYEE) {
                throw new IllegalArgumentException("Admin can send specific notification only to an employee");
            }
            notify(recipient, message);
            return;
        }

        if (request.isSendToAll()) {
            throw new UnauthorizedException("Only admin can send notifications to all users");
        }
        if (request.getRecipientUserId() == null) {
            throw new IllegalArgumentException("Recipient is required");
        }
        User recipient = userRepository.findById(request.getRecipientUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        notify(recipient, message);
    }

    @Transactional
    public Notification updateReadStatus(Long notificationId, boolean read) {
        User current = currentUserService.getCurrentUser();
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, current.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setReadFlag(read);
        return notificationRepository.save(notification);
    }
}
