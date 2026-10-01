package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.OrderStockHoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStockHoldRepository extends JpaRepository<OrderStockHoldEntity, Long> {
}
