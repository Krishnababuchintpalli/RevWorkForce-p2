package com.revworkforce.admin.dto;

import lombok.Data;

@Data
public class AssignManagerRequest {
  private Long employeeId;
  private Long managerId;
}
