package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.*;
import java.util.List;

public interface NotificationService {

  NotificationResponse createNotification(NotificationCreateRequest request);

  List<NotificationResponse> getAllNotifications();

  List<NotificationResponse> getNotificationsForEmployee(Long employeeId);

  void markAsRead(Long notificationId);

  void deleteNotification(Long notificationId);
  void sendNotificationToAll(String message);
}
