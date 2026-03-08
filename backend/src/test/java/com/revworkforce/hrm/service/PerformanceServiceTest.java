package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.ManagerPerformanceReviewRequest;
import com.revworkforce.hrm.entity.PerformanceReview;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.ReviewStatus;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.PerformanceReviewRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceServiceTest {

    @Mock
    private PerformanceReviewRepository reviewRepository;
    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PerformanceService performanceService;

    @Test
    public void adminTeamReviewsShouldReturnAll() {
        User admin = user(3001L, Role.ADMIN);
        when(currentUserService.getCurrentUser()).thenReturn(admin);
        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(new PerformanceReview()));

        Assert.assertEquals(1, performanceService.teamReviews().size());
    }

    @Test
    public void managerCreateByManagerShouldSaveAndNotify() {
        User manager = user(2001L, Role.MANAGER);
        User employee = user(1001L, Role.EMPLOYEE);
        employee.setManager(manager);

        ManagerPerformanceReviewRequest request = new ManagerPerformanceReviewRequest();
        request.setEmployeeId(1001L);
        request.setKeyDeliverables("Deliverables");
        request.setAccomplishments("Accomplishments");
        request.setAreasOfImprovement("AOI");
        request.setManagerFeedback("Good");
        request.setManagerRating(4);

        when(currentUserService.getCurrentUser()).thenReturn(manager);
        when(userRepository.findById(1001L)).thenReturn(Optional.of(employee));
        when(reviewRepository.save(any(PerformanceReview.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PerformanceReview saved = performanceService.createByManager(request);

        Assert.assertEquals(ReviewStatus.REVIEWED, saved.getStatus());
        verify(notificationService).notify(employee, "Manager submitted your performance review");
    }

    @Test
    public void managerCreateByManagerShouldFailForNonReportee() {
        User manager = user(2001L, Role.MANAGER);
        User otherManager = user(2002L, Role.MANAGER);
        User employee = user(1001L, Role.EMPLOYEE);
        employee.setManager(otherManager);

        ManagerPerformanceReviewRequest request = new ManagerPerformanceReviewRequest();
        request.setEmployeeId(1001L);
        request.setKeyDeliverables("Deliverables");
        request.setAccomplishments("Accomplishments");
        request.setAreasOfImprovement("AOI");
        request.setManagerFeedback("Good");
        request.setManagerRating(4);

        when(currentUserService.getCurrentUser()).thenReturn(manager);
        when(userRepository.findById(1001L)).thenReturn(Optional.of(employee));

        Assert.assertThrows(UnauthorizedException.class, () -> performanceService.createByManager(request));
    }

    private static User user(Long id, Role role) {
        User user = new User();
        user.setEmployeeId(String.valueOf(id));
        user.setRole(role);
        return user;
    }
}
