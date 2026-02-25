package com.revworkforce.admin.dto;

import lombok.Data;

@Data
public class EmployeeStatusUpdateRequest {
  private String status; // ACTIVE or INACTIVE
}
