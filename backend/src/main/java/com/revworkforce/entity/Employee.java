package com.revworkforce.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long employeeId;

  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @ManyToOne
  @JoinColumn(name = "dept_id")
  private Department department;

  @ManyToOne
  @JoinColumn(name = "desig_id")
  private Designation designation;

  // Self reference (Manager hierarchy)
  @ManyToOne
  @JoinColumn(name = "manager_id")
  private Employee manager;

  private String status; // ACTIVE / INACTIVE

  private String phone;
  private String address;
  private String emergencyContact;

  private LocalDateTime createdAt;
}
