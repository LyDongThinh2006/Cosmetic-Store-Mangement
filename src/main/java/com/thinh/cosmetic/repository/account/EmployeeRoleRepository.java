package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.EmployeeRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<EmployeeRoleEntity, Long> {
}
