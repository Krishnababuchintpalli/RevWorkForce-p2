package com.revworkforce.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revworkforce.hrm.dto.GoalRequest;
import com.revworkforce.hrm.entity.Goal;
import com.revworkforce.hrm.enums.GoalPriority;
import com.revworkforce.hrm.enums.GoalStatus;
import com.revworkforce.hrm.exception.GlobalExceptionHandler;
import com.revworkforce.hrm.service.GoalService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GoalControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalController goalController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(goalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }



    @Test
    public void myGoalsShouldReturnArray() throws Exception {
        when(goalService.myGoals()).thenReturn(Collections.singletonList(new Goal()));
        mockMvc.perform(get("/api/goals/my")).andExpect(status().isOk());
    }

    @Test
    public void updateStatusShouldReturnUpdatedGoal() throws Exception {
        GoalRequest.UpdateStatusRequest request = new GoalRequest.UpdateStatusRequest();
        request.setStatus(GoalStatus.IN_PROGRESS);
        request.setManagerComment("Track weekly");

        Goal goal = new Goal();
        goal.setStatus(GoalStatus.IN_PROGRESS);
        when(goalService.updateStatus(any(Long.class), any(GoalRequest.UpdateStatusRequest.class))).thenReturn(goal);

        mockMvc.perform(patch("/api/goals/12/status")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}
