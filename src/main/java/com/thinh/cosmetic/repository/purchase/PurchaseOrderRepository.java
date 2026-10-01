package com.thinh.cosmetic.repository.purchase;

import com.thinh.cosmetic.domain.entity.purchase.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {
}
