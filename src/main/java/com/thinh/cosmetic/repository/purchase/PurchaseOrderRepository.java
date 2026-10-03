package com.thinh.cosmetic.repository.purchase;

import com.thinh.cosmetic.domain.entity.inventory.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {
    List<PurchaseOrderEntity> findBySupplierId(Long supplierId);
    List<PurchaseOrderEntity> findByReceivingStoreId(Long storeId);
}
