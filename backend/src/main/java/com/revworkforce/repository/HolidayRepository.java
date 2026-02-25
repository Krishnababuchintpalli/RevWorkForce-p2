package com.revworkforce.repository;

import com.revworkforce.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

  List<Holiday> findAllByOrderByDateAsc();
}
