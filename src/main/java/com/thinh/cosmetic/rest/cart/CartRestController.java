package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;
import com.thinh.cosmetic.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestParam(required = false, defaultValue = "1") Long customerId
    ) throws Exception {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @Valid @RequestBody CartItemRequest request
    ) throws Exception {
        return ResponseEntity.ok(cartService.addItem(customerId, request));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity
    ) throws Exception {
        return ResponseEntity.ok(cartService.updateItemQuantity(customerId, cartItemId, quantity));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @PathVariable Long cartItemId
    ) throws Exception {
        return ResponseEntity.ok(cartService.removeItem(customerId, cartItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @RequestParam(required = false, defaultValue = "1") Long customerId
    ) throws Exception {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
