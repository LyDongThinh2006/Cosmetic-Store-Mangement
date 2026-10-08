package com.thinh.cosmetic.domain.dto.response.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSkuResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String skuCode;
    private String variantName;
    private BigDecimal price;
    private BigDecimal listPrice;
    private String barcode;
    private ActiveStatus status;
}
