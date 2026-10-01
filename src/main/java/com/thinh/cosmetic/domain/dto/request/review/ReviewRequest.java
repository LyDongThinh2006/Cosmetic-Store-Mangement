package com.thinh.cosmetic.domain.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReviewRequest {
    @NotNull private Long productId;
    @NotNull private Long orderId;
    @NotNull @Min(1) @Max(5) private Integer rating;
    private String comment;
}
