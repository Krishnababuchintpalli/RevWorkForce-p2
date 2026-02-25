package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.*;
import java.util.List;

public interface HolidayService {

  HolidayResponse addHoliday(HolidayCreateRequest request);

  List<HolidayResponse> getAllHolidays();

  HolidayResponse updateHoliday(Long id, HolidayCreateRequest request);

  void deleteHoliday(Long id);
}
