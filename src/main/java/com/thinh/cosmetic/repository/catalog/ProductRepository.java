package com.thinh.cosmetic.repository.catalog;

import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCategoryId(Long categoryId);
    List<ProductEntity> findByBrandId(Long brandId);
    List<ProductEntity> findByIsFeaturedTrue();
    List<ProductEntity> findByNameContainingIgnoreCase(String keyword);
}
