package com.revworkforce.admin.service.impl;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.DepartmentService;
import com.revworkforce.entity.Department;
import com.revworkforce.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  @Override
  public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

    if (departmentRepository.existsByDeptName(request.getDeptName())) {
      throw new RuntimeException("Department already exists");
    }

    Department department = new Department();
    department.setDeptName(request.getDeptName());

    Department saved = departmentRepository.save(department);

    return mapToResponse(saved);
  }

  @Override
  public List<DepartmentResponse> getAllDepartments() {
    return departmentRepository.findAll()
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  @Override
  public DepartmentResponse getDepartmentById(Long deptId) {
    Department department = departmentRepository.findById(deptId)
      .orElseThrow(() -> new RuntimeException("Department not found"));

    return mapToResponse(department);
  }

  @Override
  public DepartmentResponse updateDepartment(Long deptId, CreateDepartmentRequest request) {
    Department department = departmentRepository.findById(deptId)
      .orElseThrow(() -> new RuntimeException("Department not found"));

    department.setDeptName(request.getDeptName());

    Department updated = departmentRepository.save(department);
    return mapToResponse(updated);
  }

  @Override
  public void deleteDepartment(Long deptId) {
    Department department = departmentRepository.findById(deptId)
      .orElseThrow(() -> new RuntimeException("Department not found"));

    departmentRepository.delete(department);
  }

  private DepartmentResponse mapToResponse(Department department) {
    return DepartmentResponse.builder()
      .deptId(department.getDeptId())
      .deptName(department.getDeptName())
      .build();
  }
}
