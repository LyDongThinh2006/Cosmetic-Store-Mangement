package com.thinh.cosmetic.service.cart.impl;

import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.dto.response.cart.CartItemResponse;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.cart.CartEntity;
import com.thinh.cosmetic.domain.entity.cart.CartItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductImageEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.CartItemRepository;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.catalog.ProductSkuRepository;
import com.thinh.cosmetic.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductSkuRepository skuRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public CartResponse getCart(Long customerId) throws Exception {
        CartEntity cart = getOrCreateCart(customerId);
        return toCartResponse(cart);
    }

    @Override
    public CartResponse addItem(Long customerId, CartItemRequest request) throws Exception {
        CartEntity cart = getOrCreateCart(customerId);
        ProductSkuEntity sku = skuRepository.findById(request.getSkuId())
                .orElseThrow(() -> new Exception("Product SKU not found: " + request.getSkuId()));

        CartItemEntity item = cartItemRepository.findByCartIdAndSkuId(cart.getId(), sku.getId())
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            item = CartItemEntity.builder()
                    .cart(cart)
                    .sku(sku)
                    .quantity(request.getQuantity())
                    .build();
            cartItemRepository.save(item);
        }

        return toCartResponse(cart);
    }

    @Override
    public CartResponse updateItemQuantity(Long customerId, Long cartItemId, Integer quantity) throws Exception {
        CartEntity cart = getOrCreateCart(customerId);
        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("Cart item not found: " + cartItemId));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return toCartResponse(cart);
    }

    @Override
    public CartResponse removeItem(Long customerId, Long cartItemId) throws Exception {
        CartEntity cart = getOrCreateCart(customerId);
        cartItemRepository.deleteById(cartItemId);
        return toCartResponse(cart);
    }

    @Override
    public void clearCart(Long customerId) throws Exception {
        CartEntity cart = getOrCreateCart(customerId);
        cartItemRepository.deleteByCartId(cart.getId());
    }

    private CartEntity getOrCreateCart(Long customerId) throws Exception {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    CustomerEntity customer = customerRepository.findById(customerId).orElse(null);
                    return cartRepository.save(CartEntity.builder().customer(customer).build());
                });
    }

    private CartResponse toCartResponse(CartEntity cart) {
        List<CartItemEntity> items = cartItemRepository.findByCartId(cart.getId());
        List<CartItemResponse> itemResponses = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItems = 0;

        for (CartItemEntity item : items) {
            ProductSkuEntity sku = item.getSku();
            BigDecimal price = sku.getPrice() != null ? sku.getPrice() : BigDecimal.ZERO;
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);
            totalItems += item.getQuantity();

            String imageUrl = productImageRepository.findByProductIdAndIsPrimaryTrue(sku.getProduct().getId())
                    .map(ProductImageEntity::getImageUrl).orElse(null);

            itemResponses.add(CartItemResponse.builder()
                    .id(item.getId())
                    .skuId(sku.getId())
                    .skuCode(sku.getSkuCode())
                    .productName(sku.getProduct().getName())
                    .variantName(sku.getVariantName())
                    .price(price)
                    .quantity(item.getQuantity())
                    .subtotal(lineTotal)
                    .imageUrl(imageUrl)
                    .build());
        }

        return CartResponse.builder()
                .id(cart.getId())
                .items(itemResponses)
                .subtotal(subtotal)
                .totalItems(totalItems)
                .build();
    }
}
