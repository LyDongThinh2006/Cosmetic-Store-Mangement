package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.EmployeeStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeStoreRepository extends JpaRepository<EmployeeStoreEntity, Long> {
    List<EmployeeStoreEntity> findByEmployeeId(Long employeeId);
    void deleteByEmployeeId(Long employeeId);
}
