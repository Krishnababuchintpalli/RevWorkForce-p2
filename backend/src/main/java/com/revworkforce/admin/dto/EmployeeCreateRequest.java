package com.revworkforce.admin.dto;


import lombok.Data;

@Data
public class EmployeeCreateRequest {
  private String name;
  private String email;
  private String password;
  private Long roleId;
  private Long deptId;
  private Long desigId;
  private Long managerId;
}
