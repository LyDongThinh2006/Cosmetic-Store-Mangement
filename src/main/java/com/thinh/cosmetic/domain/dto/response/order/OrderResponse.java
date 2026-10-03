package com.thinh.cosmetic.domain.dto.response.order;

import com.thinh.cosmetic.domain.enums.DeliveryMethod;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long customerId;
    private String customerName;
    private Long fulfillmentStoreId;
    private String fulfillmentStoreName;
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
    private String ward;
    private String district;
    private String province;
    private DeliveryMethod deliveryMethod;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private List<OrderItemResponse> items;
}
