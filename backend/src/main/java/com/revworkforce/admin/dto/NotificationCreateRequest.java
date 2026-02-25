package com.revworkforce.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
  private Long recipientId;
  private String message;
}
