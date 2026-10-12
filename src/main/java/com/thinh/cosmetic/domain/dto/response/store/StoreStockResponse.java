package com.thinh.cosmetic.domain.dto.response.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreStockResponse {
    private Long storeId;
    private String storeName;
    private String address;
    private String province;
    private String phone;
    private Integer actualStock;
    private Integer heldQuantity;
    private Integer availableStock;
    private Boolean inStock;

    public boolean isInStock() {
        return Boolean.TRUE.equals(inStock);
    }
}
