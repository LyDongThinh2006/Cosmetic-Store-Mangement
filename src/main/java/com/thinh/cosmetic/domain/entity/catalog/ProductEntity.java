package com.thinh.cosmetic.domain.entity.catalog;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String name;

    private String description;

    private String origin;

    private String mainIngredients;

    private String uses;

    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    private LocalDateTime createdAt;
}
