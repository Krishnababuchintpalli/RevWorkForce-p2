package com.revworkforce.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "designations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Designation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long desigId;

  @Column(nullable = false, unique = true)
  private String desigName;
}
