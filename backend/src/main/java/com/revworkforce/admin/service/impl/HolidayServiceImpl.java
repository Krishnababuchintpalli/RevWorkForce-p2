package com.revworkforce.admin.service.impl;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.HolidayService;
import com.revworkforce.entity.Holiday;
import com.revworkforce.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

  private final HolidayRepository holidayRepository;

  @Override
  public HolidayResponse addHoliday(HolidayCreateRequest request) {

    Holiday holiday = Holiday.builder()
      .name(request.getName())
      .date(request.getDate())
      .description(request.getDescription())
      .build();

    Holiday saved = holidayRepository.save(holiday);
    return mapToResponse(saved);
  }

  @Override
  public List<HolidayResponse> getAllHolidays() {
    return holidayRepository.findAllByOrderByDateAsc()
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  @Override
  public HolidayResponse updateHoliday(Long id, HolidayCreateRequest request) {

    Holiday holiday = holidayRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Holiday not found"));

    holiday.setName(request.getName());
    holiday.setDate(request.getDate());
    holiday.setDescription(request.getDescription());

    Holiday updated = holidayRepository.save(holiday);
    return mapToResponse(updated);
  }

  @Override
  public void deleteHoliday(Long id) {

    Holiday holiday = holidayRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Holiday not found"));

    holidayRepository.delete(holiday);
  }

  private HolidayResponse mapToResponse(Holiday holiday) {
    return HolidayResponse.builder()
      .id(holiday.getId())
      .name(holiday.getName())
      .date(holiday.getDate())
      .description(holiday.getDescription())
      .build();
  }
}
