package com.thinh.cosmetic.domain.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class BeautyProfileRequest {
    private String skinType;
    private String skinConcerns;
    private String careNeeds;
    private String preferences;
    private String priceRangeInterest;
}
