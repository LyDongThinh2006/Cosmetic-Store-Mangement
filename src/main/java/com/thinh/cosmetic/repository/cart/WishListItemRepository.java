package com.thinh.cosmetic.repository.cart;

import com.thinh.cosmetic.domain.entity.catalog.WishListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishListItemRepository extends JpaRepository<WishListItemEntity, Long> {
    List<WishListItemEntity> findByWishListId(Long wishListId);
    Optional<WishListItemEntity> findByWishListIdAndProductId(Long wishListId, Long productId);
    void deleteByWishListIdAndProductId(Long wishListId, Long productId);
    void deleteByWishListId(Long wishListId);
    void deleteByProductId(Long productId);
    boolean existsByWishListIdAndProductId(Long wishListId, Long productId);
    long countByWishListId(Long wishListId);
}
