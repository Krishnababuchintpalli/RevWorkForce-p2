package com.revworkforce.hrm.service;

import com.revworkforce.hrm.dto.ManagerPerformanceReviewRequest;
import com.revworkforce.hrm.dto.ReviewFeedbackRequest;
import com.revworkforce.hrm.entity.PerformanceReview;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.enums.ReviewStatus;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.exception.UnauthorizedException;
import com.revworkforce.hrm.repository.PerformanceReviewRepository;
import com.revworkforce.hrm.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerformanceService {

    private final PerformanceReviewRepository reviewRepository;
    private final CurrentUserService currentUserService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public PerformanceService(PerformanceReviewRepository reviewRepository,
                              CurrentUserService currentUserService,
                              NotificationService notificationService,
                              UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.currentUserService = currentUserService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Transactional
    public PerformanceReview createByManager(ManagerPerformanceReviewRequest request) {
        User manager = currentUserService.getCurrentUser();
        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        boolean isAdmin = manager.getRole() == Role.ADMIN;
        boolean isManagerOfEmployee = employee.getManager() != null && employee.getManager().getId().equals(manager.getId());
        if (!isAdmin && !isManagerOfEmployee) {
            throw new UnauthorizedException("Not authorized to create review for this employee");
        }

        PerformanceReview review = new PerformanceReview();
        review.setEmployee(employee);
        review.setKeyDeliverables(request.getKeyDeliverables());
        review.setAccomplishments(request.getAccomplishments());
        review.setAreasOfImprovement(request.getAreasOfImprovement());
        review.setManagerFeedback(request.getManagerFeedback());
        review.setManagerRating(request.getManagerRating());
        review.setStatus(ReviewStatus.REVIEWED);
        PerformanceReview saved = reviewRepository.save(review);

        notificationService.notify(saved.getEmployee(), "Manager submitted your performance review");
        return saved;
    }

    public List<PerformanceReview> myReviews() {
        return reviewRepository.findByEmployeeId(currentUserService.getCurrentUser().getId());
    }

    public List<PerformanceReview> teamReviews() {
        User current = currentUserService.getCurrentUser();
        if (current.getRole() == Role.ADMIN) {
            return reviewRepository.findAll();
        }
        return reviewRepository.findByEmployeeManagerId(current.getId());
    }

    @Transactional
    public PerformanceReview feedback(Long reviewId, ReviewFeedbackRequest request) {
        PerformanceReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (review.getEmployee().getManager() == null || !review.getEmployee().getManager().getId().equals(currentUserService.getCurrentUser().getId())) {
            throw new UnauthorizedException("Not authorized to give feedback");
        }
        review.setManagerFeedback(request.getFeedback());
        review.setManagerRating(request.getRating());
        review.setStatus(ReviewStatus.REVIEWED);
        PerformanceReview saved = reviewRepository.save(review);
        notificationService.notify(saved.getEmployee(), "Manager submitted your performance feedback");
        return saved;
    }
}
