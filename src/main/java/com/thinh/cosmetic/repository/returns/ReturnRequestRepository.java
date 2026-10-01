package com.thinh.cosmetic.repository.returns;

import com.thinh.cosmetic.domain.entity.returns.ReturnRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnRequestRepository extends JpaRepository<ReturnRequestEntity, Long> {
}
