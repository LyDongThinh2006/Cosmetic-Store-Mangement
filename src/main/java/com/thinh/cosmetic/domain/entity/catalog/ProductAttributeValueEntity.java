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
@Table(name = "product_attribute_values")
public class ProductAttributeValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @NotNull(message = "Attribute cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attribute_id", nullable = false)
    private AttributeEntity attribute;

    @NotBlank(message = "Attribute value cannot be blank")
    @Size(max = 100, message = "Attribute value must not exceed 100 characters")
    @Column(name = "value", nullable = false, length = 100)
    private String value;
}
