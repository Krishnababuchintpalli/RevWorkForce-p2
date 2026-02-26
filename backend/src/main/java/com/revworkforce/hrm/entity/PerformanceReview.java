package com.revworkforce.hrm.entity;

import com.revworkforce.hrm.enums.ReviewStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_reviews")
public class PerformanceReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_employee_id")
    private Long employeeEmployeeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private User employee;

    @Column(length = 2000)
    private String keyDeliverables;

    @Column(length = 2000)
    private String accomplishments;

    @Column(length = 2000)
    private String areasOfImprovement;

    private Integer selfRating;

    @Column(length = 2000)
    private String managerFeedback;

    private Integer managerRating;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.DRAFT;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public User getEmployee() { return employee; }
    public void setEmployee(User employee) {
        this.employee = employee;
        this.employeeEmployeeId = employee != null ? employee.getId() : null;
    }
    public String getKeyDeliverables() { return keyDeliverables; }
    public void setKeyDeliverables(String keyDeliverables) { this.keyDeliverables = keyDeliverables; }
    public String getAccomplishments() { return accomplishments; }
    public void setAccomplishments(String accomplishments) { this.accomplishments = accomplishments; }
    public String getAreasOfImprovement() { return areasOfImprovement; }
    public void setAreasOfImprovement(String areasOfImprovement) { this.areasOfImprovement = areasOfImprovement; }
    public Integer getSelfRating() { return selfRating; }
    public void setSelfRating(Integer selfRating) { this.selfRating = selfRating; }
    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }
    public Integer getManagerRating() { return managerRating; }
    public void setManagerRating(Integer managerRating) { this.managerRating = managerRating; }
    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }

    @PrePersist
    @PreUpdate
    public void syncEmployeeJoinColumns() {
        if (employee != null) {
            employeeEmployeeId = employee.getId();
        }
    }
}
