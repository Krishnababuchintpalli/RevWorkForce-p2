package com.revworkforce.admin.service;


import com.revworkforce.admin.dto.EmployeeCreateRequest;
import com.revworkforce.admin.service.AdminService;
import com.revworkforce.entity.*;
import com.revworkforce.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    Role role = roleRepository.findById(request.getRoleId())
      .orElseThrow(() -> {
        log.error("Role not found with id: {}", request.getRoleId());
        return new RuntimeException("Role not found");
      });

    Department dept = departmentRepository.findById(request.getDeptId())
      .orElseThrow(() -> {
        log.error("Department not found with id: {}", request.getDeptId());
        return new RuntimeException("Department not found");
      });

    Designation desig = designationRepository.findById(request.getDesigId())
      .orElseThrow(() -> {
        log.error("Designation not found with id: {}", request.getDesigId());
        return new RuntimeException("Designation not found");
      });

    Employee manager = null;
    if (request.getManagerId() != null) {
      manager = employeeRepository.findById(request.getManagerId())
        .orElseThrow(() -> {
          log.error("Manager not found with id: {}", request.getManagerId());
          return new RuntimeException("Manager not found");
        });
    }

    Employee emp = new Employee();
    emp.setName(request.getName());
    emp.setEmail(request.getEmail());
    emp.setPassword(passwordEncoder.encode(request.getPassword()));
    emp.setRole(role);
    emp.setDepartment(dept);
    emp.setDesignation(desig);
    emp.setManager(manager);
    emp.setStatus("ACTIVE");

    employeeRepository.save(emp);

    log.info("Employee created successfully with email: {}", request.getEmail());
  }
}
