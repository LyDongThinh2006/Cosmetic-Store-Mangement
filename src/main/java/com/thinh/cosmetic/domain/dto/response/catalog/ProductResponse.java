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

    private ActiveStatus status;

    private LocalDateTime createdAt;
}
