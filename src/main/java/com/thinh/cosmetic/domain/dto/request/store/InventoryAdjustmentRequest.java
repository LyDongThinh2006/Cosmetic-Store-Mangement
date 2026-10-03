package com.thinh.cosmetic.domain.dto.request.store;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryAdjustmentRequest {
    @NotNull(message = "Store ID cannot be null")
    private Long storeId;

    @NotNull(message = "SKU ID cannot be null")
    private Long skuId;

    @NotNull(message = "New quantity cannot be null")
    @Min(value = 0, message = "New quantity must be greater than or equal to 0")
    private Integer newQuantity;

    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;
}
