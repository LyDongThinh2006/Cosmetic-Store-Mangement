package com.thinh.cosmetic.domain.entity.account;

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
@Table(name = "beauty_profiles")
public class BeautyProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    private String skinType;

    private String skinConcerns;

    private String careNeeds;

    private String preferences;

    private String priceRangeInterest;

    private LocalDateTime updatedAt;
}
