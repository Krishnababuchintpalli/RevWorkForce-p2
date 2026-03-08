package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.LeaveApplyRequest;
import com.revworkforce.hrm.dto.LeaveSummaryResponse;
import com.revworkforce.hrm.entity.LeaveRequest;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.LeaveStatus;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRepository;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    @Value("${app.leave.total-allocated:24}")
    private int totalAllocatedLeaves;

    public LeaveService(LeaveRequestRepository leaveRepository,
                        CurrentUserService currentUserService,
                        NotificationService notificationService) {
        this.leaveRepository = leaveRepository;
        this.currentUserService = currentUserService;
        this.notificationService = notificationService;
    }

    @Transactional
    public LeaveRequest apply(LeaveApplyRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        long requestedDays = calculateRequestedDays(request);
        if (requestedDays <= 0) {
            throw new IllegalArgumentException("Invalid leave date range");
        }

        LeaveSummaryResponse summary = myLeaveSummary();
        if (summary.getRemainingLeaves() <= 0) {
            throw new IllegalArgumentException("No remaining leaves available");
        }
        if (requestedDays > summary.getRemainingLeaves()) {
            throw new IllegalArgumentException("Requested leave days exceed remaining leave balance");
        }

        User current = currentUserService.getCurrentUser();
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployee(current);
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        LeaveRequest saved = leaveRepository.save(leave);

        if (current.getManager() != null) {
            safeNotify(current.getManager(), current.getFullName() + " submitted a leave request");
        }
        return saved;
    }

    public List<LeaveRequest> myLeaves() {
        return leaveRepository.findByEmployeeId(currentUserService.getCurrentUser().getId());
    }

    public LeaveSummaryResponse myLeaveSummary() {
        List<LeaveRequest> leaves = myLeaves();
        int usedDays = leaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(this::leaveDays)
                .sum();
        int pendingApprovals = (int) leaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .count();
        int pendingLeaveDays = leaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .mapToInt(this::leaveDays)
                .sum();
        int remainingLeaves = Math.max(totalAllocatedLeaves - usedDays, 0);
        return new LeaveSummaryResponse(totalAllocatedLeaves, remainingLeaves, pendingApprovals, usedDays, pendingLeaveDays);
    }

    public List<LeaveRequest> teamLeaves() {
        User current = currentUserService.getCurrentUser();
        if (current.getRole() == Role.ADMIN) {
            return leaveRepository.findAll();
        }
        return leaveRepository.findByEmployeeManagerId(current.getId());
    }

    @Transactional
    public LeaveRequest cancel(Long id) {
        LeaveRequest leave = leaveRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        if (!leave.getEmployee().getId().equals(currentUserService.getCurrentUser().getId())) {
            throw new UnauthorizedException("You can cancel only your leave requests");
        }
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new UnauthorizedException("Only pending leave can be canceled");
        }
        leave.setStatus(LeaveStatus.CANCELED);
        return leaveRepository.save(leave);
    }

    @Transactional
    public LeaveRequest approve(Long id, String comment) {
        LeaveRequest leave = leaveRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        if (!canModerateLeave(leave)) {
            throw new UnauthorizedException("Not authorized to approve this leave");
        }
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new UnauthorizedException("Only pending leave can be approved");
        }
        leave.setStatus(LeaveStatus.APPROVED);
        leave.setManagerComment(comment);
        LeaveRequest saved = leaveRepository.save(leave);
        safeNotify(leave.getEmployee(), "Your leave request was approved");
        return saved;
    }

    @Transactional
    public LeaveRequest reject(Long id, String comment) {
        if (comment == null || comment.isBlank()) {
            throw new UnauthorizedException("Rejection comment is mandatory");
        }
        LeaveRequest leave = leaveRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        if (!canModerateLeave(leave)) {
            throw new UnauthorizedException("Not authorized to reject this leave");
        }
        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new UnauthorizedException("Only pending leave can be rejected");
        }
        leave.setStatus(LeaveStatus.REJECTED);
        leave.setManagerComment(comment);
        LeaveRequest saved = leaveRepository.save(leave);
        safeNotify(leave.getEmployee(), "Your leave request was rejected");
        return saved;
    }

    private void safeNotify(User user, String message) {
        try {
            notificationService.notify(user, message);
        } catch (Exception ignored) {
            // Notification failure should not block core leave workflow.
        }
    }

    private int leaveDays(LeaveRequest leave) {
        if (leave.getStartDate() == null || leave.getEndDate() == null) {
            return 0;
        }
        if (leave.getEndDate().isBefore(leave.getStartDate())) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
    }

    private long calculateRequestedDays(LeaveApplyRequest request) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
    }

    private boolean canModerateLeave(LeaveRequest leave) {
        User current = currentUserService.getCurrentUser();
        if (current.getRole() == Role.ADMIN) {
            return true;
        }
        User manager = leave.getEmployee().getManager();
        return manager != null && manager.getId().equals(current.getId());
    }
}
