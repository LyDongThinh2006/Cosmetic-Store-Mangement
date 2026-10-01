package com.thinh.cosmetic.domain.dto.response.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class InventoryResponse {
    private Long id;
    private Long storeId;
    private String storeName;
    private Long skuId;
    private String skuCode;
    private String productName;
    private Integer actualStock;
    private Integer heldQuantity;
    private Integer availableStock;
    private Integer minimumStock;
}
