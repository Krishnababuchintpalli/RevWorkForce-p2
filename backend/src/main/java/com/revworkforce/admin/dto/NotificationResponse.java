package com.revworkforce.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
  private Long id;
  private Long recipientId;
  private String message;
  private boolean isRead;
  private LocalDateTime createdAt;
}
