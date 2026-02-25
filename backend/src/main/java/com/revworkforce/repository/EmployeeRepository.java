package com.revworkforce.repository;

import com.revworkforce.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  // Used by Spring Security to authenticate user via email
  Optional<Employee> findByEmail(String email);

  long countByStatus(String status);

  @Query("""
        SELECT e.department.deptName, COUNT(e)
        FROM Employee e
        GROUP BY e.department.deptName
    """)
  List<Object[]> countEmployeesByDepartment();

  @Query("""

    SELECT e FROM Employee e
WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
AND (:employeeId IS NULL OR CAST(e.employeeId AS string) = :employeeId)
AND (:department IS NULL OR LOWER(e.department.deptName) = LOWER(:department))
AND (:designation IS NULL OR LOWER(e.designation.desigName) = LOWER(:designation))
""")
  List<Employee> searchEmployees(
    @Param("name") String name,
    @Param("employeeId") String employeeId,
    @Param("department") String department,
    @Param("designation") String designation
  );

}
