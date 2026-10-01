package com.thinh.cosmetic.domain.entity.order;

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
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "sku_id")
    private ProductSkuEntity sku;

    private String snapshotProductName;
    private String snapshotVariantName;
    private BigDecimal snapshotPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}
