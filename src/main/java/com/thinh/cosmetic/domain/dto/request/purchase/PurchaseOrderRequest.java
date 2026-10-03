package com.thinh.cosmetic.domain.dto.request.purchase;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrderRequest {
    @NotNull(message = "Supplier ID cannot be null")
    private Long supplierId;

    @NotNull(message = "Receiving store ID cannot be null")
    private Long receivingStoreId;

    @NotEmpty(message = "Purchase items list cannot be empty")
    private List<PurchaseItemRequest> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PurchaseItemRequest {
        @NotNull(message = "SKU ID cannot be null")
        private Long skuId;

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be greater than 0")
        private Integer quantity;

        @NotNull(message = "Unit price cannot be null")
        @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0")
        private BigDecimal unitPrice;
    }
}
