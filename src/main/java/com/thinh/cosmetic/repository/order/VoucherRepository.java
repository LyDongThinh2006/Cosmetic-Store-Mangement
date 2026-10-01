package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
}
