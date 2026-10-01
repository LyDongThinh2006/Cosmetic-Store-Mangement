package com.thinh.cosmetic.domain.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CartItemRequest {
    @NotNull private Long skuId;
    @NotNull @Min(1) private Integer quantity;
}
