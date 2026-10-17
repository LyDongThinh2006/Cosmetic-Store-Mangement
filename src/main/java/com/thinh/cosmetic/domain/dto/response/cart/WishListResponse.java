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
    private Integer totalItems;

    public Integer getTotalItems() {
        if (totalItems != null) {
            return totalItems;
        }
        return items != null ? items.size() : 0;
    }

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class WishListItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String brandName;
        private BigDecimal price;
        private BigDecimal listPrice;
        private String imageUrl;
        private Long defaultSkuId;
        private Boolean inStock;
        private LocalDateTime addedAt;
    }
}
