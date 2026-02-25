package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class ReportingController {

  private final ReportingService reportingService;

  @GetMapping("/dashboard")
  public ResponseEntity<DashboardSummaryResponse> getDashboardSummary() {
    return ResponseEntity.ok(reportingService.getDashboardSummary());
  }

  @GetMapping("/employees-by-department")
  public ResponseEntity<List<DepartmentEmployeeCountResponse>> getEmployeeCountByDepartment() {
    return ResponseEntity.ok(reportingService.getEmployeeCountByDepartment());
  }
}
