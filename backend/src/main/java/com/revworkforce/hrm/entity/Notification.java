package com.revworkforce.hrm.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "employee_id")
    private User user;

    // Legacy duplicate FK columns retained in existing DBs.
    @Column(name = "user_employee_id")
    private Long legacyUserEmployeeId;

    @Column(name = "employee_id")
    private Long legacyEmployeeId;

    @Column(length = 1000)
    private String message;

    private boolean readFlag = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    private void syncLegacyUserColumns() {
        Long userId = user == null ? null : user.getId();
        legacyUserEmployeeId = userId;
        legacyEmployeeId = userId;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isReadFlag() { return readFlag; }
    public void setReadFlag(boolean readFlag) { this.readFlag = readFlag; }
}
