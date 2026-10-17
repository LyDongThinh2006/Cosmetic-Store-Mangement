package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.cart.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishListRestController {
    private final WishListService wishListService;

    private Long resolveCustomerId(AuthPrincipal principal, Long customerId) {
        if (principal != null && principal.isCustomer()) {
            if (customerId != null && !customerId.equals(principal.getCustomerId())) {
                throw new BusinessException(
                        ErrorCode.ACCESS_DENIED,
                        "Không có quyền thao tác trên danh sách yêu thích của người khác");
            }
            return principal.getCustomerId();
        }
        if (principal != null && principal.getCustomerId() != null) {
            return principal.getCustomerId();
        }
        if (customerId != null) {
            return customerId;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return 1L;
        }
        throw new BusinessException(
                ErrorCode.UNAUTHENTICATED,
                "Vui lòng đăng nhập để thao tác với danh sách yêu thích");
    }

    @GetMapping
    public ResponseEntity<WishListResponse> getWishList(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.getWishList(resolveCustomerId(principal, customerId)));
    }

    @GetMapping("/product-ids")
    public ResponseEntity<List<Long>> getWishListProductIds(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.getWishListProductIds(resolveCustomerId(principal, customerId)));
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

    @DeleteMapping
    public ResponseEntity<Void> clearWishList(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        wishListService.clearWishList(resolveCustomerId(principal, customerId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/move-all-to-cart")
    public ResponseEntity<WishListResponse> moveAllToCart(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(wishListService.moveAllToCart(resolveCustomerId(principal, customerId)));
    }
}
