package com.thinh.cosmetic.repository.order;

import com.thinh.cosmetic.domain.entity.sales.VoucherEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {

    Optional<VoucherEntity> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    List<VoucherEntity> findAllByOrderByCreatedAtDesc();

    List<VoucherEntity> findByStatusOrderByCreatedAtDesc(ActiveStatus status);
}
