package com.thinh.cosmetic.repository.purchase;

import com.thinh.cosmetic.domain.entity.inventory.PurchaseOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItemEntity, Long> {
    List<PurchaseOrderItemEntity> findByPurchaseOrderId(Long purchaseOrderId);
}
