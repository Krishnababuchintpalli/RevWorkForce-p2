package com.revworkforce.admin.service.impl;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.ReportingService;
import com.revworkforce.repository.EmployeeRepository;
import com.revworkforce.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

  private final EmployeeRepository employeeRepository;
  private final HolidayRepository holidayRepository;

  @Override
  public DashboardSummaryResponse getDashboardSummary() {

    long totalEmployees = employeeRepository.count();
    long activeEmployees = employeeRepository.countByStatus("ACTIVE");
    long inactiveEmployees = employeeRepository.countByStatus("INACTIVE");
    long totalHolidays = holidayRepository.count();

    return DashboardSummaryResponse.builder()
      .totalEmployees(totalEmployees)
      .activeEmployees(activeEmployees)
      .inactiveEmployees(inactiveEmployees)
      .totalHolidays(totalHolidays)
      .build();
  }

  @Override
  public List<DepartmentEmployeeCountResponse> getEmployeeCountByDepartment() {

    return employeeRepository.countEmployeesByDepartment()
      .stream()
      .map(obj -> DepartmentEmployeeCountResponse.builder()
        .departmentName((String) obj[0])
        .employeeCount((Long) obj[1])
        .build())
      .toList();
  }
}
