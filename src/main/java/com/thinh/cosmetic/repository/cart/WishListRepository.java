package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.catalog.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    Optional<WishListEntity> findByCustomerId(Long customerId);
}
