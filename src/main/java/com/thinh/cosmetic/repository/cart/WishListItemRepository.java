package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.cart.WishListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListItemRepository extends JpaRepository<WishListItemEntity, Long> {
}
