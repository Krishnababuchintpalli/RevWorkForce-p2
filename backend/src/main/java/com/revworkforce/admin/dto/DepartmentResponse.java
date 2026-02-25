package com.revworkforce.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
  private Long deptId;
  private String deptName;
}
