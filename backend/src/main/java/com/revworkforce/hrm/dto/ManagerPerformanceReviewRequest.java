package com.revworkforce.hrm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ManagerPerformanceReviewRequest {
    @NotNull
    private Long employeeId;
    @NotBlank
    private String keyDeliverables;
    @NotBlank
    private String accomplishments;
    @NotBlank
    private String areasOfImprovement;
    @NotBlank
    private String managerFeedback;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer managerRating;

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getKeyDeliverables() { return keyDeliverables; }
    public void setKeyDeliverables(String keyDeliverables) { this.keyDeliverables = keyDeliverables; }
    public String getAccomplishments() { return accomplishments; }
    public void setAccomplishments(String accomplishments) { this.accomplishments = accomplishments; }
    public String getAreasOfImprovement() { return areasOfImprovement; }
    public void setAreasOfImprovement(String areasOfImprovement) { this.areasOfImprovement = areasOfImprovement; }
    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }
    public Integer getManagerRating() { return managerRating; }
    public void setManagerRating(Integer managerRating) { this.managerRating = managerRating; }
}
