package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.cart.WishListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
}
