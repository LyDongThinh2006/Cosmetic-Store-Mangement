package com.thinh.cosmetic.domain.dto.request.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandRequest {
    private String name;

    private String description;

    private ActiveStatus status;
}
