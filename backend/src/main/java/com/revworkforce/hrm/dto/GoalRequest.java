package com.revworkforce.hrm.dto;

import com.revworkforce.hrm.enums.GoalPriority;
import com.revworkforce.hrm.enums.GoalStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class GoalRequest {
    @NotBlank
    private String description;
    @NotNull
    private LocalDate deadline;
    @NotNull
    private GoalPriority priority;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public GoalPriority getPriority() { return priority; }
    public void setPriority(GoalPriority priority) { this.priority = priority; }

    public static class UpdateStatusRequest {
        @NotNull
        private GoalStatus status;
        private String managerComment;

        public GoalStatus getStatus() { return status; }
        public void setStatus(GoalStatus status) { this.status = status; }
        public String getManagerComment() { return managerComment; }
        public void setManagerComment(String managerComment) { this.managerComment = managerComment; }
    }
}
