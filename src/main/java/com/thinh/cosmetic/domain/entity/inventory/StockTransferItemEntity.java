package com.thinh.cosmetic.domain.entity.inventory;

import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stock_transfer_items")
public class StockTransferItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Stock transfer cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_transfer_id", nullable = false)
    private StockTransferEntity stockTransfer;

    @NotNull(message = "SKU cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSkuEntity sku;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
