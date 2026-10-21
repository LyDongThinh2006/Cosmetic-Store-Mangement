package com.thinh.cosmetic.domain.dto.response.returns;

import com.thinh.cosmetic.domain.enums.ReturnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReturnRequestResponse {
    private Long id;
    private Long orderId;
    private String orderCode;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Long storeId;
    private String storeName;
    private String reason;
    private String description;
    private ReturnStatus status;
    private BigDecimal refundAmount;
    private LocalDateTime requestedAt;
    private String processedByName;
    private LocalDateTime processedAt;
    private List<ReturnItemResponse> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ReturnItemResponse {
        private Long id;
        private Long orderItemId;
        private Long skuId;
        private String productName;
        private String variantName;
        private String imageUrl;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal refundAmount;
        private String detailReason;
    }
}

