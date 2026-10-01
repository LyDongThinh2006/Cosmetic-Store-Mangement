package com.thinh.cosmetic.repository.store;

import com.thinh.cosmetic.domain.entity.store.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    List<InventoryEntity> findByStoreId(Long storeId);
    Optional<InventoryEntity> findByStoreIdAndSkuId(Long storeId, Long skuId);

    @Query("SELECT i FROM InventoryEntity i WHERE i.store.id = :storeId AND i.actualStock <= i.minimumStock")
    List<InventoryEntity> findLowStock(@Param("storeId") Long storeId);
}
