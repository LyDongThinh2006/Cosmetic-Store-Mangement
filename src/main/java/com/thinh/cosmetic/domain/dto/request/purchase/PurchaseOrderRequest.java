package com.thinh.cosmetic.domain.dto.request.purchase;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class PurchaseOrderRequest {
    @NotNull private Long supplierId;
    @NotNull private Long receivingStoreId;
    @NotEmpty private List<PurchaseItemRequest> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class PurchaseItemRequest {
        @NotNull private Long skuId;
        @NotNull private Integer quantity;
        @NotNull private BigDecimal unitPrice;
    }
}
