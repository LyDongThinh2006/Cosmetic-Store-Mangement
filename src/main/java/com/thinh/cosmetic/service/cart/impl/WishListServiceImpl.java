package com.thinh.cosmetic.service.cart.impl;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.cart.WishListEntity;
import com.thinh.cosmetic.domain.entity.cart.WishListItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductImageEntity;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.WishListItemRepository;
import com.thinh.cosmetic.repository.cart.WishListRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.service.cart.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public WishListResponse getWishList(Long customerId) throws Exception {
        WishListEntity wishList = getOrCreateWishList(customerId);
        return toResponse(wishList);
    }

    @Override
    public WishListResponse addProduct(Long customerId, Long productId) throws Exception {
        WishListEntity wishList = getOrCreateWishList(customerId);
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new Exception("Product not found: " + productId));

        if (wishListItemRepository.findByWishListIdAndProductId(wishList.getId(), productId).isEmpty()) {
            WishListItemEntity item = WishListItemEntity.builder()
                    .wishList(wishList)
                    .product(product)
                    .build();
            wishListItemRepository.save(item);
        }
        return toResponse(wishList);
    }

    @Override
    public WishListResponse removeProduct(Long customerId, Long productId) throws Exception {
        WishListEntity wishList = getOrCreateWishList(customerId);
        wishListItemRepository.deleteByWishListIdAndProductId(wishList.getId(), productId);
        return toResponse(wishList);
    }

    private WishListEntity getOrCreateWishList(Long customerId) throws Exception {
        return wishListRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    CustomerEntity customer = customerRepository.findById(customerId).orElse(null);
                    return wishListRepository.save(WishListEntity.builder().customer(customer).build());
                });
    }

    private WishListResponse toResponse(WishListEntity wishList) {
        List<WishListItemEntity> items = wishListItemRepository.findByWishListId(wishList.getId());
        List<WishListResponse.WishListItemResponse> itemResponses = new ArrayList<>();

        for (WishListItemEntity item : items) {
            ProductEntity product = item.getProduct();
            String imageUrl = productImageRepository.findByProductIdAndIsPrimaryTrue(product.getId())
                    .map(ProductImageEntity::getImageUrl).orElse(null);

            itemResponses.add(WishListResponse.WishListItemResponse.builder()
                    .id(item.getId())
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(BigDecimal.ZERO)
                    .imageUrl(imageUrl)
                    .addedAt(item.getAddedAt())
                    .build());
        }

        return WishListResponse.builder()
                .id(wishList.getId())
                .items(itemResponses)
                .build();
    }
}
