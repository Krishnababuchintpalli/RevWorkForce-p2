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
    public Department saveDepartment(Department d) { return departmentRepository.save(d); }

    @Transactional
    public Designation saveDesignation(Designation d) { return designationRepository.save(d); }

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
}
