package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.cart.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishListRestController {
    private final WishListService wishListService;

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
    public ResponseEntity<WishListResponse> getWishList(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.getWishList(resolveCustomerId(principal, customerId)));
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<WishListResponse> addProduct(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @PathVariable Long productId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.addProduct(resolveCustomerId(principal, customerId), productId));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<WishListResponse> removeProduct(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @PathVariable Long productId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.removeProduct(resolveCustomerId(principal, customerId), productId));
    }
}
