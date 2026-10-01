package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
