package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.StockTransferItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferItemRepository extends JpaRepository<StockTransferItemEntity, Long> {
}
