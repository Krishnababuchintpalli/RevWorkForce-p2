package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.dto.LeaveApplyRequest;
import com.revworkforce.hrm.dto.LeaveDecisionRequest;
import com.revworkforce.hrm.dto.LeaveSummaryResponse;
import com.revworkforce.hrm.entity.LeaveRequest;
import com.revworkforce.hrm.service.LeaveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    public LeaveRequest apply(@Valid @RequestBody LeaveApplyRequest request) {
        return leaveService.apply(request);
    }

    @GetMapping("/my")
    public List<LeaveRequest> myLeaves() {
        return leaveService.myLeaves();
    }

    @GetMapping("/summary")
    public LeaveSummaryResponse summary() {
        return leaveService.myLeaveSummary();
    }

    @GetMapping("/team")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<LeaveRequest> teamLeaves() {
        return leaveService.teamLeaves();
    }

    @PatchMapping("/{id}/cancel")
    public LeaveRequest cancel(@PathVariable Long id) {
        return leaveService.cancel(id);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public LeaveRequest approve(@PathVariable Long id, @RequestBody(required = false) LeaveDecisionRequest request) {
        return leaveService.approve(id, request == null ? null : request.getComment());
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public LeaveRequest reject(@PathVariable Long id, @RequestBody LeaveDecisionRequest request) {
        return leaveService.reject(id, request.getComment());
    }
}
