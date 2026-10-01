package com.thinh.cosmetic.repository.catalog;

import com.thinh.cosmetic.domain.entity.catalog.SkuAttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuAttributeValueRepository extends JpaRepository<SkuAttributeValueEntity, Long> {
}
