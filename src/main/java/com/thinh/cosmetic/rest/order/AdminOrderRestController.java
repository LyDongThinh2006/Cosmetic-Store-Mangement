package com.thinh.cosmetic.rest.order;

import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderRestController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long storeId
    ) {
        return ResponseEntity.ok(orderService.getOrders(principal, storeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        return ResponseEntity.ok(orderService.getById(principal, id));
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
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        return ResponseEntity.ok(orderService.updateStatus(principal, id, OrderStatus.CANCELLED));
    }
}
