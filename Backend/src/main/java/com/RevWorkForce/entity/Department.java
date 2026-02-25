package com.revworkforce.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "departments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deptId;

  @Column(nullable = false, unique = true)
  private String deptName;
}
