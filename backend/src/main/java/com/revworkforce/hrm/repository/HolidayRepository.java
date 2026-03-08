package com.revworkforce.hrm.repository;

import com.revworkforce.hrm.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
