package com.thinh.cosmetic.domain.dto.response.store;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    private Long id;
    private String name;
    private String address;
    private String province;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String operatingHours;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private ActiveStatus status;
}
