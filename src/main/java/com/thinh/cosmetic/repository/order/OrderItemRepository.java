package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
