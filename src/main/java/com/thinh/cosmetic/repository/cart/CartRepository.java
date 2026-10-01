package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.cart.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
