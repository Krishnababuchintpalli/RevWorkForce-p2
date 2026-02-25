package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @PostMapping
  public ResponseEntity<NotificationResponse> createNotification(
    @RequestBody NotificationCreateRequest request) {
    return ResponseEntity.ok(notificationService.createNotification(request));
  }

  @GetMapping
  public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
    return ResponseEntity.ok(notificationService.getAllNotifications());
  }

  @GetMapping("/employee/{recipientId}")
  public ResponseEntity<List<NotificationResponse>> getNotificationsForEmployee(
    @PathVariable Long recipientId) {
    return ResponseEntity.ok(notificationService.getNotificationsForEmployee(recipientId));
  }

  @PutMapping("/{id}/read")
  public ResponseEntity<String> markAsRead(@PathVariable Long id) {
    notificationService.markAsRead(id);
    return ResponseEntity.ok("Notification marked as read");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
    notificationService.deleteNotification(id);
    return ResponseEntity.ok("Notification deleted successfully");
  }

  @PostMapping("/broadcast")
  public ResponseEntity<String> sendNotificationToAll(
    @RequestBody NotificationBroadcastRequest request) {

    notificationService.sendNotificationToAll(request.getMessage());
    return ResponseEntity.ok("Notification sent to all employees");
  }
}
