package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.order.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    Optional<VoucherEntity> findByCode(String code);
    boolean existsByCode(String code);
}
