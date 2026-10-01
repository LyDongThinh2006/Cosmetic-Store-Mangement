package com.thinh.cosmetic.domain.dto.request.returns;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReturnRequestDto {
    @NotNull private Long orderId;
    private String reason;
    @NotEmpty private List<ReturnItemDto> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ReturnItemDto {
        @NotNull private Long orderItemId;
        @NotNull private Integer quantity;
        private String detailReason;
    }
}
