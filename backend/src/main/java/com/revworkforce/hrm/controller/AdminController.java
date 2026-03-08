package com.revworkforce.hrm.controller;

import com.revworkforce.hrm.entity.*;
import com.revworkforce.hrm.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/departments")
    public List<Department> departments() { return adminService.departments(); }

    @PostMapping("/departments")
    public Department saveDepartment(@RequestBody Department d) { return adminService.saveDepartment(d); }

    @DeleteMapping("/departments/{id}")
    public void deleteDepartment(@PathVariable Long id) { adminService.deleteDepartment(id); }

    @GetMapping("/designations")
    public List<Designation> designations() { return adminService.designations(); }

    @PostMapping("/designations")
    public Designation saveDesignation(@RequestBody Designation d) { return adminService.saveDesignation(d); }

    @DeleteMapping("/designations/{id}")
    public void deleteDesignation(@PathVariable Long id) { adminService.deleteDesignation(id); }

    @GetMapping("/holidays")
    public List<Holiday> holidays() { return adminService.holidays(); }

    @PostMapping("/holidays")
    public Holiday saveHoliday(@RequestBody Holiday h) { return adminService.saveHoliday(h); }

    @DeleteMapping("/holidays/{id}")
    public void deleteHoliday(@PathVariable Long id) { adminService.deleteHoliday(id); }

    @GetMapping("/announcements")
    public List<Announcement> announcements() { return adminService.announcements(); }

    @PostMapping("/announcements")
    public Announcement saveAnnouncement(@RequestBody Announcement a) { return adminService.saveAnnouncement(a); }

    @DeleteMapping("/announcements/{id}")
    public void deleteAnnouncement(@PathVariable Long id) { adminService.deleteAnnouncement(id); }
}
