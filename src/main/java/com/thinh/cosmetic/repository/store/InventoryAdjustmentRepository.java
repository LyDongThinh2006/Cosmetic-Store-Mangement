package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.inventory.InventoryAdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustmentEntity, Long> {
}
