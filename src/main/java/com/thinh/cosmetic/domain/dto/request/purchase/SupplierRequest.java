package com.thinh.cosmetic.domain.dto.request.purchase;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SupplierRequest {
    @NotBlank private String name;
    private String address;
    private String phone;
    private String email;
    private ActiveStatus status;
}
