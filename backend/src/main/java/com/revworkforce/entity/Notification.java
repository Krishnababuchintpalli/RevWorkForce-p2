package com.revworkforce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Employee recipient;

  private String message;
  private boolean isRead = false;
  private LocalDateTime createdAt = LocalDateTime.now();
}
