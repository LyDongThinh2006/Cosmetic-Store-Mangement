package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.inventory.StockTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockTransferRepository extends JpaRepository<StockTransferEntity, Long> {
    List<StockTransferEntity> findBySourceStoreIdOrDestinationStoreId(Long sourceStoreId, Long destinationStoreId);
}
