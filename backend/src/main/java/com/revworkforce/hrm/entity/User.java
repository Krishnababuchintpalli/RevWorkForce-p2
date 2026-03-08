package com.revworkforce.hrm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.revworkforce.hrm.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employees")
public class User {
    @Id
    @Column(name = "employee_id")
    private Long id;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    // Password is always stored in encrypted form and never serialized.
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String fullName;

    private String phone;
    private String address;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    private String emergencyContact;
    @Column(name = "desig_id")
    private Long designationId;
    @Column(name = "dept_id")
    private Long departmentId;
    private LocalDate joiningDate;
    private Double salary;
    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Column(name = "role_id", nullable = false)
    private Integer roleId = 1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", referencedColumnName = "employee_id")
    @JsonIgnoreProperties({"password", "manager", "reportees"})
    private User manager;

    @OneToMany(mappedBy = "manager")
    @JsonIgnore
    private Set<User> reportees = new HashSet<>();

    public Long getId() { return id; }
    @JsonProperty("employeeId")
    public String getEmployeeId() { return id == null ? null : String.valueOf(id); }
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            this.id = null;
            return;
        }
        this.id = Long.parseLong(employeeId.trim());
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    @JsonProperty("designation")
    public String getDesignation() { return designationId == null ? null : String.valueOf(designationId); }
    public void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            this.designationId = null;
            return;
        }
        this.designationId = Long.parseLong(designation.trim());
    }
    @JsonProperty("department")
    public String getDepartment() { return departmentId == null ? null : String.valueOf(departmentId); }
    public void setDepartment(String department) {
        if (department == null || department.isBlank()) {
            this.departmentId = null;
            return;
        }
        this.departmentId = Long.parseLong(department.trim());
    }
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }
    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
    public boolean isActive() { return "ACTIVE".equalsIgnoreCase(status); }
    public void setActive(boolean active) { this.status = active ? "ACTIVE" : "INACTIVE"; }
    public Role getRole() {
        if (roleId == null) return Role.EMPLOYEE;
        if (roleId == 2) return Role.MANAGER;
        if (roleId == 3) return Role.ADMIN;
        return Role.EMPLOYEE;
    }
    public void setRole(Role role) {
        if (role == null) {
            this.roleId = 1;
            return;
        }
        switch (role) {
            case ADMIN:
                this.roleId = 3;
                break;
            case MANAGER:
                this.roleId = 2;
                break;
            default:
                this.roleId = 1;
        }
    }
    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }
}
