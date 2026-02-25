package com.revworkforce.admin.service.impl;


import com.revworkforce.admin.dto.EmployeeCreateRequest;
import com.revworkforce.admin.dto.EmployeeResponse;
import com.revworkforce.admin.dto.EmployeeUpdateRequest;
import com.revworkforce.admin.service.AdminService;
import com.revworkforce.entity.*;

import com.revworkforce.entity.Role;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

  private final EmployeeRepository employeeRepository;
  private final RoleRepository roleRepository;
  private final DepartmentRepository departmentRepository;
  private final DesignationRepository designationRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void createEmployee(EmployeeCreateRequest request) {

    log.info("Admin request received to create employee with email: {}", request.getEmail());

    RoleEntity role = roleRepository.findById(request.getRoleId())
      .orElseThrow(() -> {
        log.error("Role not found with id: {}", request.getRoleId());
        return new RuntimeException("Role not found");
      });

    Department dept = departmentRepository.findById(request.getDeptId())
      .orElseThrow(() -> new RuntimeException("Department not found"));

    Designation desig = designationRepository.findById(request.getDesigId())
      .orElseThrow(() -> new RuntimeException("Designation not found"));

    Employee manager = null;
    if (request.getManagerId() != null) {
      manager = employeeRepository.findById(request.getManagerId())
        .orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    Employee emp = new Employee();

    // Determine base series based on role
    long base;
    String roleName = role.getRoleName().name();
    if ("ROLE_ADMIN".equals(roleName)) {
      base = 3000;
    } else if ("ROLE_MANAGER".equals(roleName)) {
      base = 2000;
    } else {
      base = 1000;
    }

    // Generate next ID within that series
    long count = employeeRepository.count();
    long nextId = base + count + 1;
    emp.setEmployeeId(nextId);

    emp.setName(request.getName());
    emp.setEmail(request.getEmail());
    emp.setPassword(passwordEncoder.encode(request.getPassword()));
    emp.setRole(role);
    emp.setDepartment(dept);
    emp.setDesignation(desig);
    emp.setManager(manager);
    emp.setStatus("ACTIVE");

    employeeRepository.save(emp);

    log.info("Employee created successfully with id: {}", nextId);
  }
  @Override
  public List<EmployeeResponse> getAllEmployees() {

    log.info("Admin request received to fetch all employees");

    List<Employee> employees = employeeRepository.findAll();

    List<EmployeeResponse> responseList = employees.stream()
      .map(emp -> new EmployeeResponse(
        emp.getEmployeeId(),
        emp.getName(),
        emp.getEmail(),
        emp.getRole().getRoleName().name(),
        emp.getDepartment().getDeptName(),
        emp.getDesignation().getDesigName(),
        emp.getManager() != null ? emp.getManager().getName() : null,
        emp.getStatus()
      ))
      .toList();

    log.info("Successfully fetched {} employees", responseList.size());

    return responseList;
  }

  @Override
  public void updateEmployee(Long employeeId, EmployeeUpdateRequest request) {

    log.info("Admin request received to update employee with id: {}", employeeId);

    Employee emp = employeeRepository.findById(employeeId)
      .orElseThrow(() -> {
        log.error("Employee not found with id: {}", employeeId);
        return new RuntimeException("Employee not found");
      });

    if (request.getName() != null) {
      emp.setName(request.getName());
    }

    if (request.getRoleId() != null) {
      RoleEntity role = roleRepository.findById(request.getRoleId())
        .orElseThrow(() -> {
          log.error("Role not found with id: {}", request.getRoleId());
          return new RuntimeException("Role not found");
        });
    }

    if (request.getDeptId() != null) {
      Department dept = departmentRepository.findById(request.getDeptId())
        .orElseThrow(() -> new RuntimeException("Department not found"));
      emp.setDepartment(dept);
    }

    if (request.getDesigId() != null) {
      Designation desig = designationRepository.findById(request.getDesigId())
        .orElseThrow(() -> new RuntimeException("Designation not found"));
      emp.setDesignation(desig);
    }

    if (request.getManagerId() != null) {
      Employee manager = employeeRepository.findById(request.getManagerId())
        .orElseThrow(() -> new RuntimeException("Manager not found"));
      emp.setManager(manager);
    }

    if (request.getStatus() != null) {
      emp.setStatus(request.getStatus());
    }

    employeeRepository.save(emp);

    log.info("Employee updated successfully with id: {}", employeeId);
  }
 //update employee status
  @Override
  public void updateEmployeeStatus(Long employeeId, String status) {

    log.info("Admin request received to update status for employee id: {}", employeeId);

    Employee emp = employeeRepository.findById(employeeId)
      .orElseThrow(() -> {
        log.error("Employee not found with id: {}", employeeId);
        return new RuntimeException("Employee not found");
      });

    if (!"ACTIVE".equalsIgnoreCase(status) && !"INACTIVE".equalsIgnoreCase(status)) {
      log.error("Invalid status value: {}", status);
      return;
    }

    emp.setStatus(status.toUpperCase());
    employeeRepository.save(emp);

    log.info("Employee status updated to {} for id: {}", status, employeeId);
  }

  @Override
  public void assignReportingManager(Long empId, Long managerId) {

    Employee employee = employeeRepository.findById(empId)
      .orElseThrow(() -> new RuntimeException("Employee not found"));

    Employee manager = employeeRepository.findById(managerId)
      .orElseThrow(() -> new RuntimeException("Manager not found"));

    employee.setManager(manager); // assuming ManyToOne manager mapping
    employeeRepository.save(employee);
  }


  @Override
  public List<EmployeeResponse> searchEmployees(
    String name,
    String employeeId,
    String department,
    String designation) {

    List<Employee> employees =
      employeeRepository.searchEmployees(name, employeeId, department, designation);

    return employees.stream()
      .map(this::mapToResponse)
      .toList();
  }

  private EmployeeResponse mapToResponse(Employee e) {
    return new EmployeeResponse(
      e.getEmployeeId(),
      e.getName(),
      e.getEmail(),
      e.getRole() != null ? e.getRole().getRoleName().name() : null,
      e.getDepartment() != null ? e.getDepartment().getDeptName() : null,
      e.getDesignation() != null ? e.getDesignation().getDesigName() : null,
      e.getManager() != null ? e.getManager().getName() : null,
      e.getStatus()
    );
  }

}
