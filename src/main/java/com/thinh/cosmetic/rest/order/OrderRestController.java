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
        return 1L;
    }

    @PostMapping("/preview")
    public ResponseEntity<OrderResponse> previewOrder(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @RequestBody OrderRequest request
    ) throws Exception {
        return ResponseEntity.ok(orderService.previewOrder(resolveCustomerId(principal, customerId), request));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId,
            @Valid @RequestBody OrderRequest request
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(resolveCustomerId(principal, customerId), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<OrderResponse> getByOrderCode(@PathVariable String orderCode) throws Exception {
        return ResponseEntity.ok(orderService.getByOrderCode(orderCode));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponse>> getByCustomer(@PathVariable Long customerId) {
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
    public ResponseEntity<List<OrderResponse>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) throws Exception {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long customerId
    ) throws Exception {
        return ResponseEntity.ok(orderService.cancelOrder(id, resolveCustomerId(principal, customerId)));
    }
}
