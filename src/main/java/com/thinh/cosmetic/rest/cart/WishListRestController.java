package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;
import com.thinh.cosmetic.service.cart.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishListRestController {
    private final WishListService wishListService;

    @GetMapping
    public ResponseEntity<WishListResponse> getWishList(
            @RequestParam(required = false, defaultValue = "1") Long customerId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.getWishList(customerId));
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<WishListResponse> addProduct(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @PathVariable Long productId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.addProduct(customerId, productId));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<WishListResponse> removeProduct(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @PathVariable Long productId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.removeProduct(customerId, productId));
    }
}
