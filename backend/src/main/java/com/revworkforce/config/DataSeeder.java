package com.revworkforce.config;


import com.revworkforce.entity.*;
import com.revworkforce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final RoleRepository roleRepository;
  private final DepartmentRepository departmentRepository;
  private final DesignationRepository designationRepository;

  @Override
  public void run(String... args) {

    if (roleRepository.count() == 0) {
      roleRepository.save(new Role(null, "ADMIN"));
      roleRepository.save(new Role(null, "MANAGER"));
      roleRepository.save(new Role(null, "EMPLOYEE"));
    }

    if (departmentRepository.count() == 0) {
      departmentRepository.save(new Department(null, "HR"));
      departmentRepository.save(new Department(null, "IT"));
      departmentRepository.save(new Department(null, "Finance"));
    }

    if (designationRepository.count() == 0) {
      designationRepository.save(new Designation(null, "Software Engineer"));
      designationRepository.save(new Designation(null, "HR Executive"));
      designationRepository.save(new Designation(null, "Manager"));
    }
  }
}
