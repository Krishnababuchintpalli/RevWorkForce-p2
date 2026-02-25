package com.revworkforce.repository;

import com.revworkforce.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
  boolean existsByDeptName(String deptName);
}
