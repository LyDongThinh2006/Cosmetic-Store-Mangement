package com.thinh.cosmetic.domain.entity.purchase;

import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrderEntity purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private ProductSkuEntity sku;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
