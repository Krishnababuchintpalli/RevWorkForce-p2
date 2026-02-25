package com.revworkforce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roleId;   // will auto-map to role_id

  @Enumerated(EnumType.STRING)
  @Column(unique = true)
  private RoleName roleName;
}
