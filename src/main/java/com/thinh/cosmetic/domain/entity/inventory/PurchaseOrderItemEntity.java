package com.thinh.cosmetic.domain.entity.inventory;

import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Purchase order cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrderEntity purchaseOrder;

    @NotNull(message = "SKU cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSkuEntity sku;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price cannot be null")
    @DecimalMin(value = "0.0", message = "Unit price must be greater than or equal to 0")
    @Column(name = "unit_cost", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "Subtotal cannot be null")
    @DecimalMin(value = "0.0", message = "Subtotal must be greater than or equal to 0")
    @Column(name = "line_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;
}
