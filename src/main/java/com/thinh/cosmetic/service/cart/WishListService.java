package com.thinh.cosmetic.service.cart;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;

public interface WishListService {
    WishListResponse getWishList(Long customerId) throws Exception;
    WishListResponse addProduct(Long customerId, Long productId) throws Exception;
    WishListResponse removeProduct(Long customerId, Long productId) throws Exception;
}
