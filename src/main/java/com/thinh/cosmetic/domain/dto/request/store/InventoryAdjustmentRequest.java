package com.thinh.cosmetic.domain.dto.request.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class InventoryAdjustmentRequest {
    @NotNull private Long storeId;
    @NotNull private Long skuId;
    @NotNull private Integer newQuantity;
    @NotBlank private String reason;
}
