package com.revworkforce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {

  private Long employeeId;
  private String name;
  private String email;
  private String role;
  private String department;
  private String designation;
  private String managerName;
  private String status;
}
