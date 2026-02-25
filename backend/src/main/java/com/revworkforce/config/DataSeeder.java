package com.revworkforce.config;

import com.revworkforce.entity.*;
import com.revworkforce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final DepartmentRepository departmentRepository;
  private final DesignationRepository designationRepository;
  private final EmployeeRepository employeeRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {

    // Seed only if empty (safe for multiple runs)
    if (roleRepository.count() == 0) {

      // ===== ROLES =====
      Role empRole = roleRepository.save(new Role(1L, "ROLE_EMPLOYEE"));
      Role mgrRole = roleRepository.save(new Role(2L, "ROLE_MANAGER"));
      Role adminRole = roleRepository.save(new Role(3L, "ROLE_ADMIN"));

      // ===== DEPARTMENTS =====
      Department it = departmentRepository.save(new Department(10L, "IT"));
      Department hr = departmentRepository.save(new Department(20L, "HR"));
      Department finance = departmentRepository.save(new Department(30L, "Finance"));

      // ===== DESIGNATIONS =====
      Designation se = designationRepository.save(new Designation(100L, "Software Engineer"));
      Designation tl = designationRepository.save(new Designation(200L, "Team Lead"));
      Designation hre = designationRepository.save(new Designation(300L, "HR Executive"));
      Designation adminDesig = designationRepository.save(new Designation(400L, "Administrator"));

      // ===== DEFAULT ADMIN (for first login only) =====
      Employee admin = new Employee();
      admin.setEmployeeId(3001L); // Admin ID series
      admin.setName("System Admin");
      admin.setEmail("admin@rev.com");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setRole(adminRole);
      admin.setDepartment(it);
      admin.setDesignation(adminDesig);
      admin.setManager(null);
      admin.setStatus("ACTIVE");

      employeeRepository.save(admin);
    }
  }
}
