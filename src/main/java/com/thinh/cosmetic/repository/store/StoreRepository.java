package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
}
