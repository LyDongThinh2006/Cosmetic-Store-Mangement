package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.sales.OrderReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderReservationRepository extends JpaRepository<OrderReservationEntity, Long> {
    List<OrderReservationEntity> findByOrderId(Long orderId);
}
