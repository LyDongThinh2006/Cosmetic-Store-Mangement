package com.thinh.cosmetic.domain.dto.response.store;

import com.thinh.cosmetic.domain.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class StockTransferResponse {
    private Long id;
    private String sourceStoreName;
    private String destinationStoreName;
    private TransferStatus status;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime shippedAt;
    private LocalDateTime receivedAt;
    private List<TransferItemResponse> items;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class TransferItemResponse {
        private Long skuId;
        private String skuCode;
        private String productName;
        private Integer quantity;
    }
}
