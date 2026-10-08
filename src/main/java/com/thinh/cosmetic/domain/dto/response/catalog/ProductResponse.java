package com.thinh.cosmetic.domain.dto.response.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;

    private BrandResponse brand;

    private CategoryResponse category;

    private String name;

    private String description;

    private String origin;

    private String mainIngredients;

    private String uses;

    private Boolean isFeatured;

    private ActiveStatus status;

    private String imageUrl;

    private java.util.List<String> imageUrls;

    private java.math.BigDecimal minPrice;

    private java.math.BigDecimal maxPrice;

    private java.math.BigDecimal listPrice;

    private Integer totalAvailableStock;

    private Boolean inStock;

    private java.util.List<ProductSkuResponse> skus;

    private java.util.List<String> skinTypes;

    private java.util.List<String> concerns;

    private LocalDateTime createdAt;
}
