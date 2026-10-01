package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
}
