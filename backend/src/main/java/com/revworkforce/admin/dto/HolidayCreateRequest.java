package com.revworkforce.admin.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HolidayCreateRequest {
  private String name;
  private LocalDate date;
  private String description;
}
