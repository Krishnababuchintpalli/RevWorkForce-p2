package com.revworkforce.admin.service.impl;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.NotificationService;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.Notification;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final EmployeeRepository employeeRepository;

  @Override
  public NotificationResponse createNotification(NotificationCreateRequest request) {

    Employee recipient = employeeRepository.findById(request.getRecipientId())
      .orElseThrow(() -> new RuntimeException("Employee not found"));

    Notification notification = Notification.builder()
      .recipient(recipient)
      .message(request.getMessage())
      .build();

    Notification saved = notificationRepository.save(notification);
    return mapToResponse(saved);
  }

  @Override
  public List<NotificationResponse> getAllNotifications() {
    return notificationRepository.findAll()
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  @Override
  public List<NotificationResponse> getNotificationsForEmployee(Long employeeId) {

    Employee employee = employeeRepository.findById(employeeId)
      .orElseThrow(() -> new RuntimeException("Employee not found"));

    return notificationRepository.findByRecipientOrderByCreatedAtDesc(employee)
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  @Override
  public void markAsRead(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
      .orElseThrow(() -> new RuntimeException("Notification not found"));

    notification.setRead(true);
    notificationRepository.save(notification);
  }

  @Override
  public void deleteNotification(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId)
      .orElseThrow(() -> new RuntimeException("Notification not found"));

    notificationRepository.delete(notification);
  }

  private NotificationResponse mapToResponse(Notification n) {
    return NotificationResponse.builder()
      .id(n.getId())
      .recipientId(n.getRecipient().getEmployeeId())
      .message(n.getMessage())
      .isRead(n.isRead())
      .createdAt(n.getCreatedAt())
      .build();
  }

  @Override
  public void sendNotificationToAll(String message) {

    List<Employee> employees = employeeRepository.findAll();

    List<Notification> notifications = employees.stream()
      .map(emp -> Notification.builder()
        .recipient(emp)
        .message(message)
        .build())
      .toList();

    notificationRepository.saveAll(notifications);
  }
}
