package com.thinh.cosmetic.domain.entity.inventory;

import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Store cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @NotNull(message = "SKU cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSkuEntity sku;

    @NotNull(message = "Quantity before cannot be null")
    @Min(value = 0, message = "Quantity before must be greater than or equal to 0")
    @Column(name = "quantity_before", nullable = false)
    private Integer quantityBefore;

    @NotNull(message = "Quantity after cannot be null")
    @Min(value = 0, message = "Quantity after must be greater than or equal to 0")
    @Column(name = "quantity_after", nullable = false)
    private Integer quantityAfter;

    @NotBlank(message = "Reason cannot be blank")
    @Size(max = 255, message = "Reason must not exceed 255 characters")
    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @NotNull(message = "Adjusted by employee cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adjusted_by", nullable = false)
    private EmployeeEntity performedBy;

    @CreationTimestamp
    @Column(name = "adjusted_at", nullable = false, updatable = false)
    private LocalDateTime adjustedAt;
}
