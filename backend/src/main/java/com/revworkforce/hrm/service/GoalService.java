package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.GoalRequest;
import com.revworkforce.hrm.entity.Goal;
import com.revworkforce.hrm.enums.GoalStatus;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoalService {

    private final GoalRepository goalRepository;
    private final CurrentUserService currentUserService;

    public GoalService(GoalRepository goalRepository, CurrentUserService currentUserService) {
        this.goalRepository = goalRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Goal create(GoalRequest request) {
        Goal goal = new Goal();
        goal.setEmployee(currentUserService.getCurrentUser());
        goal.setDescription(request.getDescription());
        goal.setDeadline(request.getDeadline());
        goal.setPriority(request.getPriority());
        return goalRepository.save(goal);
    }

    public List<Goal> myGoals() {
        return goalRepository.findByEmployeeId(currentUserService.getCurrentUser().getId());
    }

    public List<Goal> teamGoals() {
        return goalRepository.findByEmployeeManagerId(currentUserService.getCurrentUser().getId());
    }

    @Transactional
    public Goal updateStatus(Long id, GoalRequest.UpdateStatusRequest request) {
        Goal goal = goalRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Goal not found"));
        Long userId = currentUserService.getCurrentUser().getId();
        boolean isOwner = goal.getEmployee().getId().equals(userId);
        boolean isManager = goal.getEmployee().getManager() != null && goal.getEmployee().getManager().getId().equals(userId);

        if (!isOwner && !isManager) {
            throw new UnauthorizedException("Not authorized to update this goal");
        }

        if (goal.getStatus() == GoalStatus.COMPLETED && request.getStatus() != GoalStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed goal status cannot be changed back");
        }
        goal.setStatus(request.getStatus());
        if (isManager) {
            goal.setManagerComment(request.getManagerComment());
        }
        return goalRepository.save(goal);
    }
}
