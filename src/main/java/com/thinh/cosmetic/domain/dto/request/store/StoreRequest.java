package com.thinh.cosmetic.domain.dto.request.store;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class StoreRequest {
    @NotBlank(message = "Store name cannot be blank")
    @Size(max = 150, message = "Store name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "Province cannot be blank")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    private String province;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    private LocalTime openTime;

    private LocalTime closeTime;

    private String operatingHours;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private ActiveStatus status;
}
