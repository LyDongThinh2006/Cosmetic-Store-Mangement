package com.thinh.cosmetic.domain.dto.request.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
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
public class ProductRequest {
    @NotNull(message = "Brand ID cannot be null")
    private Long brandId;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 200, message = "Product name must not exceed 200 characters")
    private String name;

    private String description;

    @Size(max = 100, message = "Origin must not exceed 100 characters")
    private String origin;

    private String mainIngredients;

    private String uses;

    private Boolean isFeatured;

    private ActiveStatus status;
}
