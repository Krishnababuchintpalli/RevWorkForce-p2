package com.RevWorkForce.auth.repository;

import com.RevWorkForce.auth.entity.RoleEntity;
import com.RevWorkForce.auth.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
