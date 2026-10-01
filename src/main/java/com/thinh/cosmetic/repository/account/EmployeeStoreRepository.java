package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.EmployeeStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeStoreRepository extends JpaRepository<EmployeeStoreEntity, Long> {
}
