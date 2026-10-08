package com.thinh.cosmetic.domain.dto.request.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockTransferRequest {
    @NotNull(message = "Source store ID cannot be null")
    private Long sourceStoreId;

    @NotNull(message = "Destination store ID cannot be null")
    private Long destinationStoreId;

    @NotEmpty(message = "Transfer items list cannot be empty")
    private List<TransferItemRequest> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TransferItemRequest {
        @NotNull(message = "SKU ID cannot be null")
        private Long skuId;

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be greater than 0")
        private Integer quantity;
    }
}
