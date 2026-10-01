package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
