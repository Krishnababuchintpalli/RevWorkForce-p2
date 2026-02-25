package com.revworkforce.admin.controller;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/holidays")
@RequiredArgsConstructor
public class HolidayController {

  private final HolidayService holidayService;

  @PostMapping
  public ResponseEntity<HolidayResponse> addHoliday(
    @RequestBody HolidayCreateRequest request) {
    return ResponseEntity.ok(holidayService.addHoliday(request));
  }

  @GetMapping
  public ResponseEntity<List<HolidayResponse>> getAllHolidays() {
    return ResponseEntity.ok(holidayService.getAllHolidays());
  }

  @PutMapping("/{id}")
  public ResponseEntity<HolidayResponse> updateHoliday(
    @PathVariable Long id,
    @RequestBody HolidayCreateRequest request) {
    return ResponseEntity.ok(holidayService.updateHoliday(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteHoliday(@PathVariable Long id) {
    holidayService.deleteHoliday(id);
    return ResponseEntity.ok("Holiday deleted successfully");
  }
}
