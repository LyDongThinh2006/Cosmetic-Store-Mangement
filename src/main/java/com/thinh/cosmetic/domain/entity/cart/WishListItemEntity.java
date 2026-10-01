package com.thinh.cosmetic.domain.entity.cart;

import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "wishlist_items")
public class WishListItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wish_list_id")
    private WishListEntity wishList;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private LocalDateTime addedAt;
}
