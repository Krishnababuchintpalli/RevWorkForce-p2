package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {

  private final DepartmentService departmentService;

  // Create Department
  @PostMapping
  public ResponseEntity<DepartmentResponse> createDepartment(
    @RequestBody CreateDepartmentRequest request) {
    return ResponseEntity.ok(departmentService.createDepartment(request));
  }

  // Get All Departments
  @GetMapping
  public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
    return ResponseEntity.ok(departmentService.getAllDepartments());
  }

  // Get Department by ID
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable Long id) {
    return ResponseEntity.ok(departmentService.getDepartmentById(id));
  }

  // Update Department
  @PutMapping("/{id}")
  public ResponseEntity<DepartmentResponse> updateDepartment(
    @PathVariable Long id,
    @RequestBody CreateDepartmentRequest request) {
    return ResponseEntity.ok(departmentService.updateDepartment(id, request));
  }

  // Delete Department
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.ok("Department deleted successfully");
  }
}
