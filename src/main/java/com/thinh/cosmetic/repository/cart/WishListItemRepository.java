package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.cart.WishListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishListItemRepository extends JpaRepository<WishListItemEntity, Long> {
    List<WishListItemEntity> findByWishListId(Long wishListId);
    Optional<WishListItemEntity> findByWishListIdAndProductId(Long wishListId, Long productId);
    void deleteByWishListIdAndProductId(Long wishListId, Long productId);
}
