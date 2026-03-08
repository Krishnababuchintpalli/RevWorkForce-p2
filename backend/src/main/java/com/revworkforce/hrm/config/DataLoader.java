package com.revworkforce.hrm.config;

import com.revworkforce.hrm.entity.Department;
import com.revworkforce.hrm.entity.Designation;
import com.revworkforce.hrm.entity.RoleMaster;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.repository.DepartmentRepository;
import com.revworkforce.hrm.repository.DesignationRepository;
import com.revworkforce.hrm.repository.RoleMasterRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository,
                                DepartmentRepository departmentRepository,
                                DesignationRepository designationRepository,
                                RoleMasterRepository roleMasterRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleMasterRepository.count() == 0) {
                RoleMaster employeeRole = new RoleMaster();
                employeeRole.setId(1);
                employeeRole.setName("ROLE_EMPLOYEE");
                roleMasterRepository.save(employeeRole);

                RoleMaster managerRole = new RoleMaster();
                managerRole.setId(2);
                managerRole.setName("ROLE_MANAGER");
                roleMasterRepository.save(managerRole);

                RoleMaster adminRole = new RoleMaster();
                adminRole.setId(3);
                adminRole.setName("ROLE_ADMIN");
                roleMasterRepository.save(adminRole);
            }

            if (departmentRepository.count() == 0) {
                Department it = new Department();
                it.setId(10L);
                it.setName("IT");
                departmentRepository.save(it);

                Department hr = new Department();
                hr.setId(20L);
                hr.setName("HR");
                departmentRepository.save(hr);

                Department fin = new Department();
                fin.setId(30L);
                fin.setName("Finance");
                departmentRepository.save(fin);
            }

            if (designationRepository.count() == 0) {
                Designation se = new Designation();
                se.setId(100L);
                se.setName("Software Engineer");
                designationRepository.save(se);

                Designation tl = new Designation();
                tl.setId(200L);
                tl.setName("Team Lead");
                designationRepository.save(tl);

                Designation hre = new Designation();
                hre.setId(300L);
                hre.setName("HR Executive");
                designationRepository.save(hre);

                Designation adminDesig = new Designation();
                adminDesig.setId(400L);
                adminDesig.setName("Administrator");
                designationRepository.save(adminDesig);
            }

            if (userRepository.count() == 0) {
                User admin = new User();
                admin.setEmployeeId("3001");
                admin.setEmail("admin@revworkforce.com");
                admin.setFullName("System Admin");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);
                admin.setDepartment("10");
                admin.setDesignation("400");
                admin.setActive(true);
                userRepository.save(admin);

                User manager = new User();
                manager.setEmployeeId("2001");
                manager.setEmail("manager@revworkforce.com");
                manager.setFullName("Team Manager");
                manager.setPassword(passwordEncoder.encode("Manager@123"));
                manager.setRole(Role.MANAGER);
                manager.setDepartment("10");
                manager.setDesignation("200");
                manager.setActive(true);
                userRepository.save(manager);

                User employee = new User();
                employee.setEmployeeId("1001");
                employee.setEmail("employee@revworkforce.com");
                employee.setFullName("Sample Employee");
                employee.setPassword(passwordEncoder.encode("Employee@123"));
                employee.setRole(Role.EMPLOYEE);
                employee.setDepartment("10");
                employee.setDesignation("100");
                employee.setManager(manager);
                employee.setActive(true);
                userRepository.save(employee);
            }
        };
    }
}
