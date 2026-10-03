package com.thinh.cosmetic.repository.purchase;

import com.thinh.cosmetic.domain.entity.inventory.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    boolean existsByName(String name);
}
