package com.revworkforce.hrm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revworkforce.hrm.dto.NotificationSendRequest;
import com.revworkforce.hrm.entity.Notification;
import com.revworkforce.hrm.entity.User;
import com.revworkforce.hrm.exception.GlobalExceptionHandler;
import com.revworkforce.hrm.service.NotificationService;
import com.revworkforce.hrm.service.UserService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void meShouldReturnCurrentUser() throws Exception {
        User me = new User();
        me.setEmployeeId("1001");
        me.setFullName("Rahul");
        when(userService.me()).thenReturn(me);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value("1001"));
    }

    @Test
    public void notificationsShouldReturnCurrentUsersNotifications() throws Exception {
        User me = new User();
        me.setEmployeeId("1001");
        when(userService.me()).thenReturn(me);
        Notification n = new Notification();
        n.setMessage("Hello");
        when(notificationService.myNotifications(1001L)).thenReturn(Collections.singletonList(n));

        mockMvc.perform(get("/api/users/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].message").value("Hello"));
    }

    @Test
    public void sendNotificationShouldReturn200() throws Exception {
        NotificationSendRequest request = new NotificationSendRequest();
        request.setMessage("Reminder");
        request.setRecipientUserId(1002L);
        doNothing().when(notificationService).sendNotification(any(NotificationSendRequest.class));

        mockMvc.perform(post("/api/users/notifications/send")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateReadShouldReturnUpdatedNotification() throws Exception {
        Notification notification = new Notification();
        notification.setReadFlag(true);
        when(notificationService.updateReadStatus(50L, true)).thenReturn(notification);

        mockMvc.perform(patch("/api/users/notifications/50/read?read=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.readFlag").value(true));
    }
}
