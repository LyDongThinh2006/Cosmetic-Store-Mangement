package com.thinh.cosmetic.domain.entity.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "attributes")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Attribute name cannot be blank")
    @Size(max = 120, message = "Attribute name must not exceed 120 characters")
    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @NotBlank(message = "Attribute group cannot be blank")
    @Size(max = 30, message = "Attribute group must not exceed 30 characters")
    @Column(name = "attribute_group", nullable = false, length = 30)
    private String group;
}
