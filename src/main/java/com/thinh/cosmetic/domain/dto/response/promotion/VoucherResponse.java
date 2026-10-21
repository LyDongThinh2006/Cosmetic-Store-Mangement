package com.thinh.cosmetic.domain.dto.response.promotion;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private VoucherType discountType;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minOrderAmount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Integer usageLimit;
    private Integer usedCount;
    private ActiveStatus status;
    private boolean isExpired;
    private boolean isLimitReached;
    private boolean isCurrentlyActive;
    private LocalDateTime createdAt;
}
