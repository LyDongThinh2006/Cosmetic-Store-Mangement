package com.thinh.cosmetic.domain.dto.response.review;

import com.thinh.cosmetic.domain.enums.ReviewModerationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReviewResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String customerName;
    private Integer rating;
    private String comment;
    private ReviewModerationStatus moderationStatus;
    private LocalDateTime createdAt;
}
