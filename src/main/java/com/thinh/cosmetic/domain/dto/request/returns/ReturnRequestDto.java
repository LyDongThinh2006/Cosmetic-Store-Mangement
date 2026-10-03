package com.thinh.cosmetic.domain.dto.request.returns;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnRequestDto {
    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;

    private String description;

    @NotEmpty(message = "Return items list cannot be empty")
    private List<ReturnItemDto> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReturnItemDto {
        @NotNull(message = "Order item ID cannot be null")
        private Long orderItemId;

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be greater than 0")
        private Integer quantity;

        @Size(max = 255, message = "Detail reason must not exceed 255 characters")
        private String detailReason;
    }
}
