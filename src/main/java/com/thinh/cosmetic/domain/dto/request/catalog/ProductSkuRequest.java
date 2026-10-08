package com.thinh.cosmetic.domain.dto.request.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSkuRequest {
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotBlank(message = "SKU code cannot be blank")
    @Size(max = 50, message = "SKU code must not exceed 50 characters")
    private String skuCode;

    @Size(max = 150, message = "Variant name must not exceed 150 characters")
    private String variantName;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0")
    private BigDecimal price;

    @DecimalMin(value = "0.0", message = "List price must be greater than or equal to 0")
    private BigDecimal listPrice;

    @Size(max = 100, message = "Barcode must not exceed 100 characters")
    private String barcode;

    private ActiveStatus status;
}
