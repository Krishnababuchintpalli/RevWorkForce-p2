package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/designations")
@RequiredArgsConstructor
public class DesignationController {

  private final DesignationService designationService;

  // Create
  @PostMapping
  public ResponseEntity<DesignationResponse> createDesignation(
    @RequestBody CreateDesignationRequest request) {
    return ResponseEntity.ok(designationService.createDesignation(request));
  }

  // Get All
  @GetMapping
  public ResponseEntity<List<DesignationResponse>> getAllDesignations() {
    return ResponseEntity.ok(designationService.getAllDesignations());
  }

  // Get By Id
  @GetMapping("/{id}")
  public ResponseEntity<DesignationResponse> getDesignation(@PathVariable Long id) {
    return ResponseEntity.ok(designationService.getDesignationById(id));
  }

  // Update
  @PutMapping("/{id}")
  public ResponseEntity<DesignationResponse> updateDesignation(
    @PathVariable Long id,
    @RequestBody CreateDesignationRequest request) {
    return ResponseEntity.ok(designationService.updateDesignation(id, request));
  }

  // Delete
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteDesignation(@PathVariable Long id) {
    designationService.deleteDesignation(id);
    return ResponseEntity.ok("Designation deleted successfully");
  }
}
