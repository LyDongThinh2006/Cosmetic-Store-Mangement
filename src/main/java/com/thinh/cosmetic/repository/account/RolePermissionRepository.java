package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
    List<RolePermissionEntity> findByRoleId(Long roleId);
    List<RolePermissionEntity> findByRoleIdIn(Collection<Long> roleIds);
}
