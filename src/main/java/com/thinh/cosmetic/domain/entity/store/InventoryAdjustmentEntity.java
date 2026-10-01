package com.thinh.cosmetic.domain.entity.store;

import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "inventory_adjustments")
public class InventoryAdjustmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @ManyToOne
    @JoinColumn(name = "product_sku_id")
    private ProductSkuEntity productSku;

    private Integer quantityBefore;

    private Integer quantityAfter;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeEntity performedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime adjustedAt;
}
