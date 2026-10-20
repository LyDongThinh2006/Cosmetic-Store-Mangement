package com.thinh.cosmetic.domain.dto.response.promotion;

import com.thinh.cosmetic.domain.enums.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherValidationResponse {
    private boolean valid;
    private String message;
    private String code;
    private VoucherType discountType;
    private BigDecimal discountAmount;
    private BigDecimal shippingDiscountAmount;
    private BigDecimal finalSubtotal;
}
