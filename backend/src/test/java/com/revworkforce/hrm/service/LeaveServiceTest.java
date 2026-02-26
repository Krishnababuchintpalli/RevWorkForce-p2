package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.LeaveApplyRequest;
import com.revworkforce.hrm.dto.LeaveSummaryResponse;
import com.revworkforce.hrm.entity.LeaveRequest;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.LeaveStatus;
import com.revworkforce.hrm.enums.LeaveType;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.LeaveRequestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LeaveServiceTest {

    @Mock
    private LeaveRequestRepository leaveRepository;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LeaveService leaveService;

    @Test
    public void myLeaveSummaryShouldReturnCalculatedCounts() {
        ReflectionTestUtils.setField(leaveService, "totalAllocatedLeaves", 24);
        User employee = user(1001L, Role.EMPLOYEE);
        when(currentUserService.getCurrentUser()).thenReturn(employee);

        LeaveRequest approved = leave(employee, LeaveStatus.APPROVED, "2026-02-01", "2026-02-03");
        LeaveRequest pending = leave(employee, LeaveStatus.PENDING, "2026-02-10", "2026-02-11");
        when(leaveRepository.findByEmployeeId(1001L)).thenReturn(Arrays.asList(approved, pending));

        LeaveSummaryResponse summary = leaveService.myLeaveSummary();

        Assert.assertEquals(24, summary.getTotalLeaves());
        Assert.assertEquals(21, summary.getRemainingLeaves());
        Assert.assertEquals(1, summary.getPendingApprovals());
        Assert.assertEquals(3, summary.getUsedLeaves());
        Assert.assertEquals(2, summary.getPendingLeaveDays());
    }

    @Test
    public void applyShouldFailWhenRemainingLeavesAreZero() {
        ReflectionTestUtils.setField(leaveService, "totalAllocatedLeaves", 0);
        User employee = user(1001L, Role.EMPLOYEE);
        when(currentUserService.getCurrentUser()).thenReturn(employee);
        when(leaveRepository.findByEmployeeId(1001L)).thenReturn(Collections.emptyList());

        LeaveApplyRequest request = new LeaveApplyRequest();
        request.setLeaveType(LeaveType.CASUAL);
        request.setStartDate(LocalDate.of(2026, 2, 27));
        request.setEndDate(LocalDate.of(2026, 2, 28));
        request.setReason("break");

        Assert.assertThrows(IllegalArgumentException.class, () -> leaveService.apply(request));
    }

    @Test
    public void applyShouldSaveWhenRequestIsValid() {
        ReflectionTestUtils.setField(leaveService, "totalAllocatedLeaves", 24);
        User manager = user(2001L, Role.MANAGER);
        User employee = user(1001L, Role.EMPLOYEE);
        employee.setManager(manager);

        when(currentUserService.getCurrentUser()).thenReturn(employee);
        when(leaveRepository.findByEmployeeId(1001L)).thenReturn(Collections.emptyList());
        when(leaveRepository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LeaveApplyRequest request = new LeaveApplyRequest();
        request.setLeaveType(LeaveType.CASUAL);
        request.setStartDate(LocalDate.of(2026, 2, 27));
        request.setEndDate(LocalDate.of(2026, 2, 28));
        request.setReason("break");

        LeaveRequest saved = leaveService.apply(request);

        Assert.assertEquals(LeaveType.CASUAL, saved.getLeaveType());
        verify(notificationService).notify(manager, "User-1001 submitted a leave request");
    }

    @Test
    public void approveShouldFailForNonManagerAndNonAdmin() {
        User manager = user(2001L, Role.MANAGER);
        User employee = user(1001L, Role.EMPLOYEE);
        employee.setManager(manager);
        LeaveRequest request = leave(employee, LeaveStatus.PENDING, "2026-02-27", "2026-02-28");
        ReflectionTestUtils.setField(request, "id", 10L);

        User randomEmployee = user(1002L, Role.EMPLOYEE);
        when(currentUserService.getCurrentUser()).thenReturn(randomEmployee);
        when(leaveRepository.findById(10L)).thenReturn(Optional.of(request));

        Assert.assertThrows(UnauthorizedException.class, () -> leaveService.approve(10L, "ok"));
    }

    private static User user(Long id, Role role) {
        User user = new User();
        user.setEmployeeId(String.valueOf(id));
        user.setFullName("User-" + id);
        user.setRole(role);
        return user;
    }

    private static LeaveRequest leave(User user, LeaveStatus status, String start, String end) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(user);
        leave.setStatus(status);
        leave.setStartDate(LocalDate.parse(start));
        leave.setEndDate(LocalDate.parse(end));
        leave.setLeaveType(LeaveType.CASUAL);
        return leave;
    }
}
