package com.thinh.cosmetic.repository.purchase;

import com.thinh.cosmetic.domain.entity.purchase.PurchaseOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItemEntity, Long> {
}
