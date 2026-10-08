package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
