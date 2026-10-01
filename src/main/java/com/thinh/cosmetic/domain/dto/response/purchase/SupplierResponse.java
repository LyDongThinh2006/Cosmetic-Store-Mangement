package com.thinh.cosmetic.domain.dto.response.purchase;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SupplierResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private ActiveStatus status;
}
