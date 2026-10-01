package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerAddressRequest {
    @NotBlank private String recipientName;
    @NotBlank private String phone;
    @NotBlank private String province;
    @NotBlank private String district;
    @NotBlank private String ward;
    @NotBlank private String addressDetail;
    private Boolean isDefault;
}
