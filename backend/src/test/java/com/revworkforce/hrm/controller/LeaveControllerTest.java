package com.revworkforce.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revworkforce.hrm.dto.LeaveApplyRequest;
import com.revworkforce.hrm.dto.LeaveSummaryResponse;
import com.revworkforce.hrm.entity.LeaveRequest;
import com.revworkforce.hrm.enums.LeaveStatus;
import com.revworkforce.hrm.enums.LeaveType;
import com.revworkforce.hrm.exception.GlobalExceptionHandler;
import com.revworkforce.hrm.service.LeaveService;
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
public class LeaveControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LeaveService leaveService;

    @InjectMocks
    private LeaveController leaveController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(leaveController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void summaryShouldReturnLeaveCounts() throws Exception {
        when(leaveService.myLeaveSummary()).thenReturn(new LeaveSummaryResponse(24, 20, 1, 4, 2));

        mockMvc.perform(get("/api/leaves/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLeaves").value(24))
                .andExpect(jsonPath("$.remainingLeaves").value(20));
    }



    @Test
    public void myLeavesShouldReturnArray() throws Exception {
        when(leaveService.myLeaves()).thenReturn(Collections.singletonList(new LeaveRequest()));
        mockMvc.perform(get("/api/leaves/my"))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectShouldForwardCommentToService() throws Exception {
        LeaveRequest leave = new LeaveRequest();
        leave.setStatus(LeaveStatus.REJECTED);
        when(leaveService.reject(10L, "Need coverage")).thenReturn(leave);

        mockMvc.perform(patch("/api/leaves/10/reject")
                        .contentType(APPLICATION_JSON)
                        .content("{\"comment\":\"Need coverage\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }
}
