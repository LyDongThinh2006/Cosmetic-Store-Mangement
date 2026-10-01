package com.thinh.cosmetic.domain.entity.order;

import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import com.thinh.cosmetic.domain.enums.HoldStatus;
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
@Table(name = "order_stock_holds")
public class OrderStockHoldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private ProductSkuEntity sku;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private HoldStatus status;
}
