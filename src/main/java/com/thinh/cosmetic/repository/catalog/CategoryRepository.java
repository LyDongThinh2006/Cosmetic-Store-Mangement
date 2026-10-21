package com.thinh.cosmetic.repository.catalog;

import com.thinh.cosmetic.domain.entity.catalog.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByParentId(Long parentId);
    List<CategoryEntity> findByStatus(com.thinh.cosmetic.domain.enums.ActiveStatus status);
    java.util.Optional<CategoryEntity> findFirstByName(String name);
    boolean existsByName(String name);
}
