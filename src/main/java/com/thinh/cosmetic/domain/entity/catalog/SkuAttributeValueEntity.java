package com.thinh.cosmetic.domain.entity.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sku_attribute_values")
public class SkuAttributeValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "SKU cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sku_id", nullable = false)
    private ProductSkuEntity sku;

    @NotNull(message = "Attribute cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false)
    private AttributeEntity attribute;

    @NotBlank(message = "Attribute value cannot be blank")
    @Size(max = 255, message = "Attribute value must not exceed 255 characters")
    @Column(name = "value", nullable = false, length = 255)
    private String value;
}
