package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.dto.GoalRequest;
import com.revworkforce.hrm.entity.Goal;
import com.revworkforce.hrm.service.GoalService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public Goal create(@Valid @RequestBody GoalRequest request) {
        return goalService.create(request);
    }

    @GetMapping("/my")
    public List<Goal> myGoals() {
        return goalService.myGoals();
    }

    @GetMapping("/team")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<Goal> teamGoals() {
        return goalService.teamGoals();
    }

    @PatchMapping("/{id}/status")
    public Goal updateStatus(@PathVariable Long id, @Valid @RequestBody GoalRequest.UpdateStatusRequest request) {
        return goalService.updateStatus(id, request);
    }
}
