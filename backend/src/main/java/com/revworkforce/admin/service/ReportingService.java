package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.*;

import java.util.List;

public interface ReportingService {

  DashboardSummaryResponse getDashboardSummary();

  List<DepartmentEmployeeCountResponse> getEmployeeCountByDepartment();
}
