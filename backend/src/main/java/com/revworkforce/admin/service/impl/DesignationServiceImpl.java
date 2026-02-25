package com.revworkforce.admin.service.impl;

import com.revworkforce.admin.dto.*;
import com.revworkforce.admin.service.DesignationService;
import com.revworkforce.entity.Designation;
import com.revworkforce.repository.DesignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

  private final DesignationRepository designationRepository;

  @Override
  public DesignationResponse createDesignation(CreateDesignationRequest request) {

    if (designationRepository.existsByDesigName(request.getDesigName())) {
      throw new RuntimeException("Designation already exists");
    }

    Designation designation = new Designation();
    designation.setDesigName(request.getDesigName());

    Designation saved = designationRepository.save(designation);
    return mapToResponse(saved);
  }

  @Override
  public List<DesignationResponse> getAllDesignations() {
    return designationRepository.findAll()
      .stream()
      .map(this::mapToResponse)
      .toList();
  }

  @Override
  public DesignationResponse getDesignationById(Long id) {
    Designation designation = designationRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Designation not found"));

    return mapToResponse(designation);
  }

  @Override
  public DesignationResponse updateDesignation(Long id, CreateDesignationRequest request) {
    Designation designation = designationRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Designation not found"));

    designation.setDesigName(request.getDesigName());

    Designation updated = designationRepository.save(designation);
    return mapToResponse(updated);
  }

  @Override
  public void deleteDesignation(Long id) {
    Designation designation = designationRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Designation not found"));

    designationRepository.delete(designation);
  }

  private DesignationResponse mapToResponse(Designation designation) {
    return DesignationResponse.builder()
      .desigId(designation.getDesigId())
      .desigName(designation.getDesigName())
      .build();
  }
}
