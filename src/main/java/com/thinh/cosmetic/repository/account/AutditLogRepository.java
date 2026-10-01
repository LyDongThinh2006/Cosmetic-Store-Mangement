package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutditLogRepository extends JpaRepository<AuditLogEntity, Long> {
}
