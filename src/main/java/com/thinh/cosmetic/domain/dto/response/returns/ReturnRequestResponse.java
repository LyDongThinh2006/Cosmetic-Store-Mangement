package com.thinh.cosmetic.domain.dto.response.returns;

import com.thinh.cosmetic.domain.enums.ReturnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ReturnRequestResponse {
    private Long id;
    private Long orderId;
    private String reason;
    private ReturnStatus status;
    private LocalDateTime requestedAt;
    private String processedByName;
    private LocalDateTime processedAt;
    private List<ReturnItemResponse> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class ReturnItemResponse {
        private Long id;
        private Long orderItemId;
        private String productName;
        private Integer quantity;
        private String detailReason;
    }
}
