package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.EmployeeCreateRequest;
import java.util.List;
import com.revworkforce.admin.dto.EmployeeResponse;
import com.revworkforce.admin.dto.EmployeeUpdateRequest;

public interface AdminService {


  List<EmployeeResponse> getAllEmployees();
  void createEmployee(EmployeeCreateRequest request);
  void updateEmployee(Long employeeId, EmployeeUpdateRequest request);
  void updateEmployeeStatus(Long employeeId, String status);
  void assignReportingManager(Long empId, Long managerId);
  List<EmployeeResponse> searchEmployees(String name, String employeeId, String department, String designation);
}
