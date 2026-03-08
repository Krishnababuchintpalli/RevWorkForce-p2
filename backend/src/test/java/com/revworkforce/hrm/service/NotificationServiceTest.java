package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.NotificationSendRequest;
import com.revworkforce.hrm.entity.Notification;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.NotificationRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void adminSendToAllShouldSendOnlyToActiveUsers() {
        User admin = user(3001L, Role.ADMIN, true);
        User activeEmployee = user(1001L, Role.EMPLOYEE, true);
        User inactiveEmployee = user(1002L, Role.EMPLOYEE, false);

        NotificationSendRequest request = new NotificationSendRequest();
        request.setMessage("Announcement");
        request.setSendToAll(true);

        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(userRepository.findAll()).thenReturn(Arrays.asList(activeEmployee, inactiveEmployee));
        when(notificationRepository.saveAndFlush(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        notificationService.sendNotification(request);

        verify(notificationRepository, times(1)).saveAndFlush(any(Notification.class));
    }

    @Test
    public void nonAdminSendToAllShouldFail() {
        User manager = user(2001L, Role.MANAGER, true);
        NotificationSendRequest request = new NotificationSendRequest();
        request.setMessage("Hi");
        request.setSendToAll(true);

        when(currentUserService.getCurrentUser()).thenReturn(manager);

        Assert.assertThrows(UnauthorizedException.class, () -> notificationService.sendNotification(request));
    }

    @Test
    public void sendSpecificShouldFailWhenRecipientMissing() {
        User employee = user(1001L, Role.EMPLOYEE, true);
        NotificationSendRequest request = new NotificationSendRequest();
        request.setMessage("Hi");
        request.setRecipientUserId(9999L);

        when(currentUserService.getCurrentUser()).thenReturn(employee);
        when(userRepository.findById(9999L)).thenReturn(Optional.empty());

        Assert.assertThrows(ResourceNotFoundException.class, () -> notificationService.sendNotification(request));
    }

    private static User user(Long id, Role role, boolean active) {
        User user = new User();
        user.setEmployeeId(String.valueOf(id));
        user.setRole(role);
        user.setActive(active);
        return user;
    }
}
