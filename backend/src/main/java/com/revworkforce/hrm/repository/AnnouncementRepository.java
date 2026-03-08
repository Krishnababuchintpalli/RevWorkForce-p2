package com.revworkforce.hrm.repository;

import com.revworkforce.hrm.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
