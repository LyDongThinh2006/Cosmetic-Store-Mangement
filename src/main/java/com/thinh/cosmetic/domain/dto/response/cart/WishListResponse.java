package com.thinh.cosmetic.domain.dto.response.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class WishListResponse {
    private Long id;
    private List<WishListItemResponse> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class WishListItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal price;
        private String imageUrl;
        private LocalDateTime addedAt;
    }
}
