package com.thinh.cosmetic.domain.dto.request.store;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class StockTransferRequest {
    @NotNull private Long sourceStoreId;
    @NotNull private Long destinationStoreId;
    @NotEmpty private List<TransferItemRequest> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class TransferItemRequest {
        @NotNull private Long skuId;
        @NotNull private Integer quantity;
    }
}
