package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.StockTransferItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockTransferItemRepository extends JpaRepository<StockTransferItemEntity, Long> {
    List<StockTransferItemEntity> findByStockTransferId(Long transferId);
}
