package com.thinh.cosmetic.domain.dto.response.purchase;

import com.thinh.cosmetic.domain.enums.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class PurchaseOrderResponse {
    private Long id;
    private String supplierName;
    private String receivingStoreName;
    private PurchaseOrderStatus status;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private BigDecimal totalAmount;
    private List<PurchaseItemResponse> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class PurchaseItemResponse {
        private Long skuId;
        private String skuCode;
        private String productName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }
}
