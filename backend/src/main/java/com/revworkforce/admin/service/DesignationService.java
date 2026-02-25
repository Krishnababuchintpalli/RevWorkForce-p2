package com.revworkforce.admin.service;

import com.revworkforce.admin.dto.*;
import java.util.List;

public interface DesignationService {

  DesignationResponse createDesignation(CreateDesignationRequest request);

  List<DesignationResponse> getAllDesignations();

  DesignationResponse getDesignationById(Long id);

  DesignationResponse updateDesignation(Long id, CreateDesignationRequest request);

  void deleteDesignation(Long id);
}
