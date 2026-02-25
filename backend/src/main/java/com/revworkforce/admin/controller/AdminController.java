package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping("/employees")
  public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

    log.info("GET /api/admin/employees called");

    List<EmployeeResponse> employees = adminService.getAllEmployees();

    log.info("GET /api/admin/employees completed successfully");

    return ResponseEntity.ok(employees);
  }

  @PutMapping("/employees/{id}")
  public ResponseEntity<String> updateEmployee(
    @PathVariable Long id,
    @RequestBody EmployeeUpdateRequest request) {

    log.info("PUT /api/admin/employees/{} called", id);

    adminService.updateEmployee(id, request);
    return ResponseEntity.ok("Employee updated successfully");
  }

  @PutMapping("/employees/{id}/status")
  public ResponseEntity<String> updateEmployeeStatus(
    @PathVariable Long id,
    @RequestBody EmployeeStatusUpdateRequest request) {

    log.info("PATCH /api/admin/employees/{}/status called", id);

    adminService.updateEmployeeStatus(id, request.getStatus());
    return ResponseEntity.ok("Employee status updated successfully");
  }




  @GetMapping("/employees/search")
  public ResponseEntity<List<EmployeeResponse>> searchEmployees(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String employeeId,
    @RequestParam(required = false) String department,
    @RequestParam(required = false) String designation) {

    return ResponseEntity.ok(
      adminService.searchEmployees(name, employeeId, department, designation));
  }

  @PutMapping("/employees/manager")
  public ResponseEntity<String> assignReportingManager(
    @RequestBody AssignManagerRequest request) {

    log.info("PUT /api/admin/employees/manager called for empId={}, managerId={}",
      request.getEmployeeId(), request.getManagerId());

    adminService.assignReportingManager(request.getEmployeeId(), request.getManagerId());

    return ResponseEntity.ok("Reporting manager assigned successfully");
  }


}
