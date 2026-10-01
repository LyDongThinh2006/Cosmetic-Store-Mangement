package com.thinh.cosmetic.domain.dto.request.store;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class StoreRequest {
    @NotBlank private String name;
    private String address;
    private String phone;
    private String operatingHours;
    private Double latitude;
    private Double longitude;
    private ActiveStatus status;
}
