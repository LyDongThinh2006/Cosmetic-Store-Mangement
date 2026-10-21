package com.thinh.cosmetic.rest.cart;

import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;
import com.thinh.cosmetic.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {
    private final CartService cartService;

    private Long resolveCustomerId(AuthPrincipal principal, Long customerId) {
        if (principal == null) {
            if (customerId != null) {
                return customerId;
            }
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Vui lòng đăng nhập để thao tác giỏ hàng");
        }
        if (principal.isCustomer()) {
            if (customerId != null && !customerId.equals(principal.getCustomerId())) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không có quyền thao tác trên giỏ hàng của khách hàng khác");
            }
            return principal.getCustomerId();
        }
        if (principal.isEmployee()) {
            boolean hasManage = principal.getAuthorities() != null &&
                    principal.getAuthorities().stream().anyMatch(a ->
                            "CUSTOMER_MANAGE".equals(a.getAuthority()) || "ORDER_MANAGE".equals(a.getAuthority()) || "ROLE_ADMIN".equals(a.getAuthority()) || "ADMIN".equals(a.getAuthority()));
            if (!hasManage) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Nhân viên không có quyền quản lý giỏ hàng của khách hàng");
            }
            if (customerId != null) {
                return customerId;
            }
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Cần cung cấp customerId khi nhân viên thao tác giỏ hàng");
        }
        if (principal.getCustomerId() != null) {
            return principal.getCustomerId();
        }
        throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        if (principal != null && principal.isEmployee() && customerId == null) {
            return ResponseEntity.ok(CartResponse.builder()
                    .items(java.util.Collections.emptyList())
                    .totalItems(0)
                    .subtotal(java.math.BigDecimal.ZERO)
                    .build());
        }
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
