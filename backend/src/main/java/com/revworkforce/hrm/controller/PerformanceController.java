package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.dto.ManagerPerformanceReviewRequest;
import com.revworkforce.hrm.dto.ReviewFeedbackRequest;
import com.revworkforce.hrm.entity.PerformanceReview;
import com.revworkforce.hrm.service.PerformanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public PerformanceReview submit(@Valid @RequestBody ManagerPerformanceReviewRequest request) {
        return performanceService.createByManager(request);
    }

    @GetMapping("/my")
    public List<PerformanceReview> my() {
        return performanceService.myReviews();
    }

    @GetMapping("/team")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public List<PerformanceReview> team() {
        return performanceService.teamReviews();
    }

    @PatchMapping("/{id}/feedback")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public PerformanceReview feedback(@PathVariable Long id, @Valid @RequestBody ReviewFeedbackRequest request) {
        return performanceService.feedback(id, request);
    }
}
