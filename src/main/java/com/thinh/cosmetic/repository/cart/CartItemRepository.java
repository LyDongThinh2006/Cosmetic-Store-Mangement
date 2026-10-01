package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
}
