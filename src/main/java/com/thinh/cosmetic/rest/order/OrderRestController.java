package com.thinh.cosmetic.rest.order;

import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.service.order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;

    private Long resolveCustomerId(AuthPrincipal principal, Long customerId) {
        if (principal != null && principal.getCustomerId() != null) {
            return principal.getCustomerId();
        }
        if (customerId != null) {
            return customerId;
        }
        throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng");
    }

    @PostMapping("/preview")
    public ResponseEntity<OrderResponse> previewOrder(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @RequestBody OrderRequest request
    ) throws Exception {
        return ResponseEntity.ok(orderService.previewOrder(resolveCustomerId(principal, customerId), request));
    }

    @PostMapping({"", "/checkout"})
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @Valid @RequestBody OrderRequest request
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(resolveCustomerId(principal, customerId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        OrderResponse order = orderService.getById(id);
        if (principal != null && principal.isCustomer() && principal.getCustomerId() != null) {
            if (order.getCustomerId() != null && !principal.getCustomerId().equals(order.getCustomerId())) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không thể xem đơn hàng của tài khoản khác");
            }
        } else if (principal != null && principal.isEmployee()) {
            return ResponseEntity.ok(orderService.getById(principal, id));
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderResponse> getByOrderCode(
            @PathVariable String orderCode,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        OrderResponse order = orderService.getByOrderCode(orderCode);
        if (principal != null && principal.isCustomer() && principal.getCustomerId() != null) {
            if (order.getCustomerId() != null && !principal.getCustomerId().equals(order.getCustomerId())) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không thể xem đơn hàng của tài khoản khác");
            }
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getByCustomer(
            @PathVariable Long customerId,
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        if (principal != null && principal.isCustomer() && !customerId.equals(principal.getCustomerId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không thể xem đơn hàng của tài khoản khác");
        }
        return ResponseEntity.ok(orderService.getByCustomer(customerId));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) {
        return ResponseEntity.ok(orderService.getByCustomer(resolveCustomerId(principal, customerId)));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long storeId
    ) {
        return ResponseEntity.ok(orderService.getOrders(principal, storeId));
    }

    @RequestMapping(value = "/{id}/status", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        return ResponseEntity.ok(orderService.updateStatus(principal, id, status));
    }

    @RequestMapping(value = "/{id}/cancel", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(orderService.cancelOrder(id, resolveCustomerId(principal, customerId)));
    }
}
