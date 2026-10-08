package com.thinh.cosmetic.repository.catalog;

import com.thinh.cosmetic.domain.entity.catalog.ProductAttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValueEntity, Long> {
    List<ProductAttributeValueEntity> findByProductId(Long productId);
}
