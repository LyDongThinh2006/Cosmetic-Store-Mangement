package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}
