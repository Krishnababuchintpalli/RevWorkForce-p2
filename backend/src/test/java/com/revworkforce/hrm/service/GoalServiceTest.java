package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.GoalRequest;
import com.revworkforce.hrm.entity.Goal;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.GoalPriority;
import com.revworkforce.hrm.enums.GoalStatus;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.GoalRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;
    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private GoalService goalService;

    @Test
    public void createShouldPersistGoalForCurrentUser() {
        User employee = user(1001L, Role.EMPLOYEE);
        GoalRequest request = new GoalRequest();
        request.setDescription("Improve automation");
        request.setDeadline(LocalDate.of(2026, 9, 1));
        request.setPriority(GoalPriority.HIGH);

        when(currentUserService.getCurrentUser()).thenReturn(employee);
        when(goalRepository.save(any(Goal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Goal goal = goalService.create(request);
        Assert.assertEquals("Improve automation", goal.getDescription());
        Assert.assertEquals(employee.getId(), goal.getEmployee().getId());
    }

    @Test
    public void updateStatusShouldNotAllowCompletedToRevert() {
        User employee = user(1001L, Role.EMPLOYEE);
        Goal goal = new Goal();
        ReflectionTestUtils.setField(goal, "id", 50L);
        goal.setEmployee(employee);
        goal.setStatus(GoalStatus.COMPLETED);

        GoalRequest.UpdateStatusRequest request = new GoalRequest.UpdateStatusRequest();
        request.setStatus(GoalStatus.IN_PROGRESS);

        when(currentUserService.getCurrentUser()).thenReturn(employee);
        when(goalRepository.findById(50L)).thenReturn(Optional.of(goal));

        Assert.assertThrows(IllegalArgumentException.class, () -> goalService.updateStatus(50L, request));
    }

    @Test
    public void updateStatusShouldFailForUnauthorizedUser() {
        User owner = user(1001L, Role.EMPLOYEE);
        User manager = user(2001L, Role.MANAGER);
        owner.setManager(manager);

        Goal goal = new Goal();
        ReflectionTestUtils.setField(goal, "id", 51L);
        goal.setEmployee(owner);
        goal.setStatus(GoalStatus.NOT_STARTED);

        User randomUser = user(1002L, Role.EMPLOYEE);

        GoalRequest.UpdateStatusRequest request = new GoalRequest.UpdateStatusRequest();
        request.setStatus(GoalStatus.IN_PROGRESS);

        when(currentUserService.getCurrentUser()).thenReturn(randomUser);
        when(goalRepository.findById(51L)).thenReturn(Optional.of(goal));

        Assert.assertThrows(UnauthorizedException.class, () -> goalService.updateStatus(51L, request));
    }

    private static User user(Long id, Role role) {
        User user = new User();
        user.setEmployeeId(String.valueOf(id));
        user.setRole(role);
        return user;
    }
}
