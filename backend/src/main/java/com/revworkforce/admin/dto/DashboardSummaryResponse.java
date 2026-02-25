package com.revworkforce.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
  private long totalEmployees;
  private long activeEmployees;
  private long inactiveEmployees;
  private long totalHolidays;
}
