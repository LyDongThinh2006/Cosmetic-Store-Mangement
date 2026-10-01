package com.thinh.cosmetic.repository.returns;

import com.thinh.cosmetic.domain.entity.returns.ReturnRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequestEntity, Long> {
    List<ReturnRequestEntity> findByOrderId(Long orderId);

    @Query("SELECT r FROM ReturnRequestEntity r WHERE r.order.customer.id = :customerId")
    List<ReturnRequestEntity> findByCustomerId(@Param("customerId") Long customerId);
}
