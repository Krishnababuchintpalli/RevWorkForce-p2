package com.revworkforce.config;


import com.revworkforce.entity.Department;
import com.revworkforce.entity.Designation;
import com.revworkforce.entity.Employee;
import com.revworkforce.entity.RoleEntity;
import com.revworkforce.entity.RoleName;
import com.revworkforce.repository.DepartmentRepository;
import com.revworkforce.repository.DesignationRepository;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.RoleRepository;
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
    if (departmentRepository.count() == 0) {

      // ===== ROLES =====
      RoleEntity empRole = new RoleEntity();
      empRole.setRoleName(RoleName.EMPLOYEE);
      empRole = roleRepository.save(empRole);

      RoleEntity mgrRole = new RoleEntity();
      mgrRole.setRoleName(RoleName.MANAGER);
      mgrRole = roleRepository.save(mgrRole);

      RoleEntity adminRole = new RoleEntity();
      adminRole.setRoleName(RoleName.ADMIN);
      adminRole = roleRepository.save(adminRole);

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
