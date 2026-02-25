package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.*;
import java.util.List;

public interface DepartmentService {

  DepartmentResponse createDepartment(CreateDepartmentRequest request);

  List<DepartmentResponse> getAllDepartments();

  DepartmentResponse getDepartmentById(Long deptId);

  DepartmentResponse updateDepartment(Long deptId, CreateDepartmentRequest request);

  void deleteDepartment(Long deptId);
}
