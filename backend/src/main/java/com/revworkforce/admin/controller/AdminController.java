package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.EmployeeCreateRequest;
import com.revworkforce.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private static final Logger log = LoggerFactory.getLogger(AdminController.class);

  private final AdminService adminService;

  @PostMapping("/employees")
  public ResponseEntity<String> createEmployee(@RequestBody EmployeeCreateRequest request) {

    log.info("POST /api/admin/employees called for email: {}", request.getEmail());

    adminService.createEmployee(request);

    log.info("POST /api/admin/employees completed successfully");

    return ResponseEntity.ok("Employee created successfully");
  }
}
