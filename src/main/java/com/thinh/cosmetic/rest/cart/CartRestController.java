package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;
import com.thinh.cosmetic.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thinh.cosmetic.security.AuthPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {
    private final CartService cartService;

    private Long resolveCustomerId(AuthPrincipal principal, Long customerId) {
        if (principal != null && principal.getCustomerId() != null) {
            return principal.getCustomerId();
        }
        if (customerId != null) {
            return customerId;
        }
        return 1L;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(cartService.getCart(resolveCustomerId(principal, customerId)));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @Valid @RequestBody CartItemRequest request
    ) throws Exception {
        return ResponseEntity.ok(cartService.addItem(resolveCustomerId(principal, customerId), request));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity
    ) throws Exception {
        return ResponseEntity.ok(cartService.updateItemQuantity(resolveCustomerId(principal, customerId), cartItemId, quantity));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @PathVariable Long cartItemId
    ) throws Exception {
        return ResponseEntity.ok(cartService.removeItem(resolveCustomerId(principal, customerId), cartItemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        cartService.clearCart(resolveCustomerId(principal, customerId));
        return ResponseEntity.noContent().build();
    }
}
