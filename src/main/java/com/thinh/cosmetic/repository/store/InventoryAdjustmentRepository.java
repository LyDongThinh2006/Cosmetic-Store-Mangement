package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.InventoryAdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustmentEntity, Long> {
}
