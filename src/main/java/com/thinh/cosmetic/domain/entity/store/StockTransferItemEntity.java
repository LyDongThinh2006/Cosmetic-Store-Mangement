package com.thinh.cosmetic.domain.entity.store;

import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "stock_transfer_id")
    private StockTransferEntity stockTransfer;

    @ManyToOne
    @JoinColumn(name = "product_sku_id")
    private ProductSkuEntity productSku;

    private Integer quantity;
}
