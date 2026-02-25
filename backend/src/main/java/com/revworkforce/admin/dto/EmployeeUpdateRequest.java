package com.revworkforce.admin.dto;

import lombok.Data;

@Data
public class EmployeeUpdateRequest {
  private String name;
  private Long roleId;
  private Long deptId;
  private Long desigId;
  private Long managerId;
  private String status; // ACTIVE / INACTIVE
}
