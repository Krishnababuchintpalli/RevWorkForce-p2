package com.revworkforce.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revworkforce.hrm.dto.ManagerPerformanceReviewRequest;
import com.revworkforce.hrm.entity.PerformanceReview;
import com.revworkforce.hrm.enums.ReviewStatus;
import com.revworkforce.hrm.exception.GlobalExceptionHandler;
import com.revworkforce.hrm.service.PerformanceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class PerformanceControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PerformanceService performanceService;

    @InjectMocks
    private PerformanceController performanceController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(performanceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void submitShouldReturnSavedReview() throws Exception {
        ManagerPerformanceReviewRequest request = new ManagerPerformanceReviewRequest();
        request.setEmployeeId(1001L);
        request.setKeyDeliverables("Delivery");
        request.setAccomplishments("Done");
        request.setAreasOfImprovement("Speed");
        request.setManagerFeedback("Good");
        request.setManagerRating(4);

        PerformanceReview review = new PerformanceReview();
        review.setStatus(ReviewStatus.REVIEWED);
        review.setManagerRating(4);
        when(performanceService.createByManager(any(ManagerPerformanceReviewRequest.class))).thenReturn(review);

        mockMvc.perform(post("/api/performance")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REVIEWED"))
                .andExpect(jsonPath("$.managerRating").value(4));
    }

    @Test
    public void myShouldReturnArray() throws Exception {
        when(performanceService.myReviews()).thenReturn(Collections.singletonList(new PerformanceReview()));
        mockMvc.perform(get("/api/performance/my")).andExpect(status().isOk());
    }
}
