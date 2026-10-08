package com.thinh.cosmetic.domain.entity.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "beauty_profiles")
public class BeautyProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer cannot be null")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private CustomerEntity customer;

    @Size(max = 255, message = "Skin type must not exceed 255 characters")
    @Column(name = "skin_type", length = 255)
    private String skinType;

    @Size(max = 255, message = "Skin concerns must not exceed 255 characters")
    @Column(name = "skin_concerns", length = 255)
    private String skinConcerns;

    @Size(max = 255, message = "Care needs must not exceed 255 characters")
    @Column(name = "care_needs", length = 255)
    private String careNeeds;

    @Size(max = 255, message = "Preferences must not exceed 255 characters")
    @Column(name = "preferences", length = 255)
    private String preferences;

    @Size(max = 100, message = "Price range must not exceed 100 characters")
    @Column(name = "price_range", length = 100)
    private String priceRangeInterest;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
