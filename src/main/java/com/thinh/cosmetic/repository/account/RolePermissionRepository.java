package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
}
