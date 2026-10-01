package com.thinh.cosmetic.service.cart;

import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;

public interface CartService {
    CartResponse getCart(Long customerId) throws Exception;
    CartResponse addItem(Long customerId, CartItemRequest request) throws Exception;
    CartResponse updateItemQuantity(Long customerId, Long cartItemId, Integer quantity) throws Exception;
    CartResponse removeItem(Long customerId, Long cartItemId) throws Exception;
    void clearCart(Long customerId) throws Exception;
}
