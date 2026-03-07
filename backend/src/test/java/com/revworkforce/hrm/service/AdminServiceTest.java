package com.revworkforce.hrm.service;

import com.revworkforce.hrm.entity.Department;
import com.revworkforce.hrm.entity.Designation;
import com.revworkforce.hrm.repository.AnnouncementRepository;
import com.revworkforce.hrm.repository.DepartmentRepository;
import com.revworkforce.hrm.repository.DesignationRepository;
import com.revworkforce.hrm.repository.HolidayRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private DesignationRepository designationRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationService notificationService;

    private AdminService adminService;

    @Before
    public void setUp() {
        adminService = new AdminService(
                departmentRepository,
                designationRepository,
                holidayRepository,
                announcementRepository,
                userRepository,
                notificationService
        );
    }

    @Test
    public void saveDepartmentShouldAutoAssignNextIdAndTrimName() {
        Department existing = new Department();
        existing.setId(30L);
        when(departmentRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(existing));
        when(departmentRepository.findByNameIgnoreCase("QA")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Department request = new Department();
        request.setName("  QA  ");

        Department saved = adminService.saveDepartment(request);

        Assert.assertEquals(Long.valueOf(40L), saved.getId());
        Assert.assertEquals("QA", saved.getName());
    }

    @Test
    public void saveDepartmentShouldUseDefaultIdWhenNoRecordsExist() {
        when(departmentRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(departmentRepository.findByNameIgnoreCase("Operations")).thenReturn(Optional.empty());
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Department request = new Department();
        request.setName("Operations");

        Department saved = adminService.saveDepartment(request);

        Assert.assertEquals(Long.valueOf(10L), saved.getId());
    }

    @Test
    public void saveDesignationShouldAutoAssignNextIdAndTrimName() {
        Designation existing = new Designation();
        existing.setId(400L);
        when(designationRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(existing));
        when(designationRepository.findByNameIgnoreCase("Principal Engineer")).thenReturn(Optional.empty());
        when(designationRepository.save(any(Designation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Designation request = new Designation();
        request.setName("  Principal Engineer  ");

        Designation saved = adminService.saveDesignation(request);

        Assert.assertEquals(Long.valueOf(500L), saved.getId());
        Assert.assertEquals("Principal Engineer", saved.getName());
    }
}
