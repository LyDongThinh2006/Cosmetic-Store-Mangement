package com.thinh.cosmetic.repository.returns;

import com.thinh.cosmetic.domain.entity.returns.ReturnItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnItemRepository extends JpaRepository<ReturnItemEntity, Long> {
}
