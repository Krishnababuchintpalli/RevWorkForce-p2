package com.revworkforce.hrm.entity;

import com.revworkforce.hrm.enums.GoalPriority;
import com.revworkforce.hrm.enums.GoalStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_employee_id")
    private Long employeeEmployeeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private User employee;

    @Column(length = 2000)
    private String description;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private GoalPriority priority;

    @Enumerated(EnumType.STRING)
    private GoalStatus status = GoalStatus.NOT_STARTED;

    @Column(length = 2000)
    private String managerComment;

    public Long getId() { return id; }
    public User getEmployee() { return employee; }
    public void setEmployee(User employee) {
        this.employee = employee;
        this.employeeEmployeeId = employee != null ? employee.getId() : null;
    }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public GoalPriority getPriority() { return priority; }
    public void setPriority(GoalPriority priority) { this.priority = priority; }
    public GoalStatus getStatus() { return status; }
    public void setStatus(GoalStatus status) { this.status = status; }
    public String getManagerComment() { return managerComment; }
    public void setManagerComment(String managerComment) { this.managerComment = managerComment; }

    @PrePersist
    @PreUpdate
    public void syncEmployeeJoinColumns() {
        if (employee != null) {
            employeeEmployeeId = employee.getId();
        }
    }
}
