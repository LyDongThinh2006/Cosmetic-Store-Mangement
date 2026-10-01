package com.thinh.cosmetic.domain.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class BeautyProfileResponse {
    private Long id;
    private String skinType;
    private String skinConcerns;
    private String careNeeds;
    private String preferences;
    private String priceRangeInterest;
    private LocalDateTime updatedAt;
}
