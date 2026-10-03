package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
