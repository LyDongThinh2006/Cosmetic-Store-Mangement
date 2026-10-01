package com.thinh.cosmetic.domain.dto.request.order;

import com.thinh.cosmetic.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class OrderRequest {
    @NotNull private Long customerAddressId;
    @NotNull private PaymentMethod paymentMethod;
    private String note;
}
