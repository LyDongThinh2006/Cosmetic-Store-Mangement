package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.OrderStockHoldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderStockHoldRepository extends JpaRepository<OrderStockHoldEntity, Long> {
    List<OrderStockHoldEntity> findByOrderId(Long orderId);
}
