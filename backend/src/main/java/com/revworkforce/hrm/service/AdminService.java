package com.revworkforce.hrm.service;

import com.revworkforce.hrm.entity.*;
import com.revworkforce.hrm.enums.Role;
import com.revworkforce.hrm.exception.ResourceNotFoundException;
import com.revworkforce.hrm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final HolidayRepository holidayRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final Object departmentLock = new Object();
    private final Object designationLock = new Object();

    public AdminService(DepartmentRepository departmentRepository,
                        DesignationRepository designationRepository,
                        HolidayRepository holidayRepository,
                        AnnouncementRepository announcementRepository,
                        UserRepository userRepository,
                        NotificationService notificationService) {
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
        this.holidayRepository = holidayRepository;
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<Department> departments() { return departmentRepository.findAll(); }
    public List<Designation> designations() { return designationRepository.findAll(); }
    public List<Holiday> holidays() { return holidayRepository.findAll(); }
    public List<Announcement> announcements() { return announcementRepository.findAll(); }

    @Transactional
    public Department saveDepartment(Department d) {
        d.setName(normalizeName(d.getName()));
        if (d.getName() == null || d.getName().isEmpty()) {
            throw new IllegalArgumentException("Department name is required.");
        }

        Department duplicate = departmentRepository.findByNameIgnoreCase(d.getName()).orElse(null);
        if (duplicate != null && (d.getId() == null || !duplicate.getId().equals(d.getId()))) {
            throw new IllegalArgumentException("Department already exists.");
        }

        synchronized (departmentLock) {
            if (d.getId() == null) {
                d.setId(nextDepartmentId());
            }
            return departmentRepository.save(d);
        }
    }

    @Transactional
    public Designation saveDesignation(Designation d) {
        d.setName(normalizeName(d.getName()));
        if (d.getName() == null || d.getName().isEmpty()) {
            throw new IllegalArgumentException("Designation name is required.");
        }

        Designation duplicate = designationRepository.findByNameIgnoreCase(d.getName()).orElse(null);
        if (duplicate != null && (d.getId() == null || !duplicate.getId().equals(d.getId()))) {
            throw new IllegalArgumentException("Designation already exists.");
        }

        synchronized (designationLock) {
            if (d.getId() == null) {
                d.setId(nextDesignationId());
            }
            return designationRepository.save(d);
        }
    }

    @Transactional
    public Holiday saveHoliday(Holiday h) { return holidayRepository.save(h); }

    @Transactional
    public Announcement saveAnnouncement(Announcement a) {
        Announcement saved = announcementRepository.save(a);
        String message = "New announcement: " + saved.getTitle();

        userRepository.findAll().stream()
                .filter(User::isActive)
                .filter(u -> u.getRole() != Role.ADMIN)
                .forEach(u -> notificationService.notify(u, message));

        return saved;
    }

    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) throw new ResourceNotFoundException("Department not found");
        departmentRepository.deleteById(id);
    }

    @Transactional
    public void deleteDesignation(Long id) {
        if (!designationRepository.existsById(id)) throw new ResourceNotFoundException("Designation not found");
        designationRepository.deleteById(id);
    }

    @Transactional
    public void deleteHoliday(Long id) {
        if (!holidayRepository.existsById(id)) throw new ResourceNotFoundException("Holiday not found");
        holidayRepository.deleteById(id);
    }

    @Transactional
    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) throw new ResourceNotFoundException("Announcement not found");
        announcementRepository.deleteById(id);
    }

    private String normalizeName(String value) {
        if (value == null) return null;
        return value.trim();
    }

    private Long nextDepartmentId() {
        Long candidate = departmentRepository.findTopByOrderByIdDesc()
                .map(existing -> existing.getId() + 10L)
                .orElse(10L);
        while (departmentRepository.existsById(candidate)) {
            candidate += 10L;
        }
        return candidate;
    }

    private Long nextDesignationId() {
        Long candidate = designationRepository.findTopByOrderByIdDesc()
                .map(existing -> existing.getId() + 100L)
                .orElse(100L);
        while (designationRepository.existsById(candidate)) {
            candidate += 100L;
        }
        return candidate;
    }
}
