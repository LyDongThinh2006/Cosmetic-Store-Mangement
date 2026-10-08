package com.thinh.cosmetic.service.order;

import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Long customerId, OrderRequest request) throws Exception;
    OrderResponse previewOrder(Long customerId, OrderRequest request) throws Exception;
    OrderResponse getById(Long id) throws Exception;
    OrderResponse getByOrderCode(String orderCode) throws Exception;
    List<OrderResponse> getByCustomer(Long customerId);
    List<OrderResponse> getAll();
    List<OrderResponse> getOrders(com.thinh.cosmetic.security.AuthPrincipal principal, Long storeId);
    OrderResponse updateStatus(Long id, OrderStatus status) throws Exception;
    OrderResponse cancelOrder(Long id, Long customerId) throws Exception;
}
