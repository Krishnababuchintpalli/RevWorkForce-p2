package com.revworkforce.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEmployeeCountResponse {
  private String departmentName;
  private long employeeCount;
}
