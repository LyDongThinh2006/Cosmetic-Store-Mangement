package com.thinh.cosmetic.service.order.impl;

import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderItemResponse;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import com.thinh.cosmetic.domain.entity.sales.CartItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderReservationEntity;

import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.ReservationStatus;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.repository.account.CustomerAddressRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.CartItemRepository;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.order.OrderReservationRepository;

import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.service.order.OrderService;

import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.security.StoreScope;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderReservationRepository stockHoldRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final StoreRepository storeRepository;
    private final InventoryRepository inventoryRepository;
    private final StoreScope storeScope;


    @Override
    public OrderResponse placeOrder(Long customerId, OrderRequest request) throws Exception {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found: " + customerId));

        CustomerAddressEntity address = addressRepository.findById(request.getCustomerAddressId())
                .orElseThrow(() -> new Exception("Address not found: " + request.getCustomerAddressId()));

        CartEntity cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new Exception("Cart not found for customer"));

        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new Exception("Cart is empty");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItemEntity item : cartItems) {
            BigDecimal price = item.getSku().getPrice() != null ? item.getSku().getPrice() : BigDecimal.ZERO;
            subtotal = subtotal.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        String voucher = request.getVoucherCode() != null ? request.getVoucherCode().trim().toUpperCase() : "";
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal freeShippingThreshold = new BigDecimal("500000");
        BigDecimal shippingFee = subtotal.compareTo(freeShippingThreshold) >= 0 ? BigDecimal.ZERO : new BigDecimal("30000");

        if ("LUNEA10".equals(voucher)) {
            discountAmount = subtotal.multiply(new BigDecimal("0.10")).min(new BigDecimal("100000"));
        } else if ("GIAM50K".equals(voucher)) {
            discountAmount = new BigDecimal("50000");
        } else if ("FREESHIP".equals(voucher)) {
            shippingFee = BigDecimal.ZERO;
        }
        BigDecimal totalAmount = subtotal.subtract(discountAmount).add(shippingFee).max(BigDecimal.ZERO);

        StoreEntity assignedStore = null;
        if (request.getFulfillmentStoreId() != null) {
            assignedStore = storeRepository.findById(request.getFulfillmentStoreId()).orElse(null);
        }
        if (assignedStore == null) {
            List<StoreEntity> stores = storeRepository.findAll();
            for (StoreEntity store : stores) {
                boolean canFulfillAll = true;
                for (CartItemEntity item : cartItems) {
                    var inv = inventoryRepository.findByStoreIdAndSkuId(store.getId(), item.getSku().getId());
                    if (inv.isEmpty() || (inv.get().getActualStock() - inv.get().getHeldQuantity()) < item.getQuantity()) {
                        canFulfillAll = false;
                        break;
                    }
                }
                if (canFulfillAll) {
                    assignedStore = store;
                    break;
                }
            }
            if (assignedStore == null && !stores.isEmpty()) {
                assignedStore = stores.get(0);
            }
        }

        String orderCode = "LUN-" + (int)(Math.random() * 900000 + 100000);

        OrderEntity order = OrderEntity.builder()
                .orderCode(orderCode)
                .customer(customer)
                .assignedStore(assignedStore)
                .recipientName(address.getRecipientName())
                .recipientPhone(address.getPhone())
                .shippingAddress(address.getAddressDetail())
                .ward(address.getWard())
                .district(address.getDistrict())
                .province(address.getProvince())
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .shippingFee(shippingFee)
                .totalAmount(totalAmount)
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .note(request.getNote())
                .build();
        order = orderRepository.save(order);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity item : cartItems) {
            ProductSkuEntity sku = item.getSku();
            BigDecimal price = sku.getPrice() != null ? sku.getPrice() : BigDecimal.ZERO;
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(order)
                    .sku(sku)
                    .snapshotProductName(sku.getProduct().getName())
                    .snapshotVariantName(sku.getVariantName())
                    .snapshotPrice(price)
                    .quantity(item.getQuantity())
                    .subtotal(lineTotal)
                    .build();
            orderItems.add(orderItemRepository.save(orderItem));

            // Reserve stock (inventory hold)
            if (assignedStore != null) {
                stockHoldRepository.save(OrderReservationEntity.builder()
                        .order(order)
                        .sku(sku)
                        .store(assignedStore)
                        .quantity(item.getQuantity())
                        .status(ReservationStatus.HELD)
                        .build());

                inventoryRepository.findByStoreIdAndSkuId(assignedStore.getId(), sku.getId())
                        .ifPresent(inv -> {
                            inv.setHeldQuantity(inv.getHeldQuantity() + item.getQuantity());
                            inventoryRepository.save(inv);
                        });
            }
        }
        order.setItems(orderItems);

        // Clear cart
        cartItemRepository.deleteByCartId(cart.getId());

        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse previewOrder(Long customerId, OrderRequest request) throws Exception {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new Exception("Customer not found: " + customerId));

        CartEntity cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new Exception("Cart not found for customer"));

        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new Exception("Cart is empty");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (CartItemEntity item : cartItems) {
            ProductSkuEntity sku = item.getSku();
            BigDecimal price = sku.getPrice() != null ? sku.getPrice() : BigDecimal.ZERO;
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);

            itemResponses.add(OrderItemResponse.builder()
                    .id(item.getId())
                    .skuId(sku.getId())
                    .productName(sku.getProduct().getName())
                    .variantName(sku.getVariantName())
                    .price(price)
                    .quantity(item.getQuantity())
                    .subtotal(lineTotal)
                    .build());
        }

        String voucher = request.getVoucherCode() != null ? request.getVoucherCode().trim().toUpperCase() : "";
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal freeShippingThreshold = new BigDecimal("500000");
        BigDecimal shippingFee = subtotal.compareTo(freeShippingThreshold) >= 0 ? BigDecimal.ZERO : new BigDecimal("30000");

        if ("LUNEA10".equals(voucher)) {
            discountAmount = subtotal.multiply(new BigDecimal("0.10")).min(new BigDecimal("100000"));
        } else if ("GIAM50K".equals(voucher)) {
            discountAmount = new BigDecimal("50000");
        } else if ("FREESHIP".equals(voucher)) {
            shippingFee = BigDecimal.ZERO;
        }
        BigDecimal totalAmount = subtotal.subtract(discountAmount).add(shippingFee).max(BigDecimal.ZERO);

        return OrderResponse.builder()
                .subtotal(subtotal)
                .discountAmount(discountAmount)
                .shippingFee(shippingFee)
                .totalAmount(totalAmount)
                .items(itemResponses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) throws Exception {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found: " + id));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getByOrderCode(String orderCode) throws Exception {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new Exception("Order not found with code: " + orderCode));
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getByCustomer(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(AuthPrincipal principal, Long requestedStoreId) {
        if (principal == null) {
            return getAll();
        }

        List<Long> accessibleIds = storeScope.getAccessibleStoreIds(principal);
        if (accessibleIds == null) {
            // Admin or ALL_STORES
            if (requestedStoreId != null) {
                return orderRepository.findByAssignedStoreIdOrderByCreatedAtDesc(requestedStoreId)
                        .stream().map(this::toResponse).toList();
            }
            return orderRepository.findAllByOrderByCreatedAtDesc()
                    .stream().map(this::toResponse).toList();
        }

        if (accessibleIds.isEmpty()) {
            return List.of();
        }

        if (requestedStoreId != null) {
            if (!accessibleIds.contains(requestedStoreId)) {
                return List.of();
            }
            return orderRepository.findByAssignedStoreIdOrderByCreatedAtDesc(requestedStoreId)
                    .stream().map(this::toResponse).toList();
        }

        return orderRepository.findByAssignedStoreIdInOrderByCreatedAtDesc(accessibleIds)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, OrderStatus status) throws Exception {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found: " + id));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);

        if (status == OrderStatus.CONFIRMED) {
            order.setConfirmedAt(LocalDateTime.now());
        } else if (status == OrderStatus.SHIPPING || status == OrderStatus.COMPLETED) {
            if (status == OrderStatus.COMPLETED) {
                order.setCompletedAt(LocalDateTime.now());
            }
            // Deduct actual stock on shipping or completed if not already committed
            List<OrderReservationEntity> holds = stockHoldRepository.findByOrderId(id);
            for (OrderReservationEntity hold : holds) {
                if (hold.getStatus() == ReservationStatus.HELD) {
                    hold.setStatus(ReservationStatus.COMMITTED);
                    stockHoldRepository.save(hold);

                    inventoryRepository.findByStoreIdAndSkuId(hold.getStore().getId(), hold.getSku().getId())
                            .ifPresent(inv -> {
                                inv.setActualStock(Math.max(0, inv.getActualStock() - hold.getQuantity()));
                                inv.setHeldQuantity(Math.max(0, inv.getHeldQuantity() - hold.getQuantity()));
                                inventoryRepository.save(inv);
                            });
                }
            }
        } else if (status == OrderStatus.CANCELLED) {
            order.setCancelledAt(LocalDateTime.now());
            // Release stock hold
            List<OrderReservationEntity> holds = stockHoldRepository.findByOrderId(id);
            for (OrderReservationEntity hold : holds) {
                if (hold.getStatus() == ReservationStatus.HELD) {
                    hold.setStatus(ReservationStatus.RELEASED);
                    stockHoldRepository.save(hold);

                    inventoryRepository.findByStoreIdAndSkuId(hold.getStore().getId(), hold.getSku().getId())
                            .ifPresent(inv -> {
                                inv.setHeldQuantity(Math.max(0, inv.getHeldQuantity() - hold.getQuantity()));
                                inventoryRepository.save(inv);
                            });
                }
            }
        }

        return toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse cancelOrder(Long id, Long customerId) throws Exception {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("Order not found: " + id));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new Exception("You do not have permission to cancel this order");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new Exception("Only pending orders can be cancelled");
        }

        return updateStatus(id, OrderStatus.CANCELLED);
    }

    private OrderResponse toResponse(OrderEntity o) {
        List<OrderItemEntity> items = o.getItems() != null ? o.getItems() :
                orderItemRepository.findByOrderId(o.getId());

        List<OrderItemResponse> itemResponses = items.stream()
                .map(i -> OrderItemResponse.builder()
                        .id(i.getId())
                        .skuId(i.getSku().getId())
                        .productName(i.getSnapshotProductName())
                        .variantName(i.getSnapshotVariantName())
                        .price(i.getSnapshotPrice())
                        .quantity(i.getQuantity())
                        .subtotal(i.getSubtotal())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(o.getId())
                .orderCode(o.getOrderCode())
                .customerId(o.getCustomer() != null ? o.getCustomer().getId() : null)
                .customerName(o.getCustomer() != null ? o.getCustomer().getFullName() : null)
                .fulfillmentStoreId(o.getAssignedStore() != null ? o.getAssignedStore().getId() : null)
                .fulfillmentStoreName(o.getAssignedStore() != null ? o.getAssignedStore().getName() : null)
                .recipientName(o.getRecipientName())
                .recipientPhone(o.getRecipientPhone())
                .shippingAddress(o.getShippingAddress() + (o.getWard() != null ? ", " + o.getWard() : "") + (o.getDistrict() != null ? ", " + o.getDistrict() : "") + (o.getProvince() != null ? ", " + o.getProvince() : ""))
                .ward(o.getWard())
                .district(o.getDistrict())
                .province(o.getProvince())
                .subtotal(o.getSubtotal())
                .discountAmount(o.getDiscountAmount())
                .shippingFee(o.getShippingFee())
                .totalAmount(o.getTotalAmount())
                .status(o.getStatus())
                .paymentMethod(o.getPaymentMethod())
                .note(o.getNote())
                .createdAt(o.getCreatedAt())
                .confirmedAt(o.getConfirmedAt())
                .completedAt(o.getCompletedAt())
                .cancelledAt(o.getCancelledAt())
                .items(itemResponses)
                .build();
    }
}
