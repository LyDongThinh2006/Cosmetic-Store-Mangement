package com.thinh.cosmetic.repository.account;

import com.thinh.cosmetic.domain.entity.account.BeautyProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BeautyProfileRepository extends JpaRepository<BeautyProfileEntity, Long> {
    Optional<BeautyProfileEntity> findByCustomerId(Long customerId);
}
