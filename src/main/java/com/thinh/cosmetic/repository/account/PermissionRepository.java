package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
}
