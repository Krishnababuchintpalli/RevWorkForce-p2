package com.revworkforce.admin.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayResponse {
  private Long id;
  private String name;
  private LocalDate date;
  private String description;
}
