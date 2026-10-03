package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.sales.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCartId(Long cartId);
    Optional<CartItemEntity> findByCartIdAndSkuId(Long cartId, Long skuId);
    void deleteByCartId(Long cartId);
}
