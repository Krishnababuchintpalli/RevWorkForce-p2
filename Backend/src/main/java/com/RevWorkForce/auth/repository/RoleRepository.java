package com.revworkforce.auth.repository;



import com.revworkforce.entity.RoleEntity;
import com.revworkforce.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);

}
