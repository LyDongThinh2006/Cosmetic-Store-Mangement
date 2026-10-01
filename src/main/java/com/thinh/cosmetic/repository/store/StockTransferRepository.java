package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.StockTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferRepository extends JpaRepository<StockTransferEntity, Long> {
}
