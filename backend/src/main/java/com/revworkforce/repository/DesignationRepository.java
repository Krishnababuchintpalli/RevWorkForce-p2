package com.revworkforce.repository;

import com.revworkforce.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
  boolean existsByDesigName(String desigName);
}
