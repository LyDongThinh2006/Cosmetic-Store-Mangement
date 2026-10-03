package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrderId(Long orderId);
}
