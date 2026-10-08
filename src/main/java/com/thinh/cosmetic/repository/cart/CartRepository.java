package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByCustomerId(Long customerId);
}
