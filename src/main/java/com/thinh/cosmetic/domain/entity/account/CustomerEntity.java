package com.thinh.cosmetic.domain.entity.account;

import com.thinh.cosmetic.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

    private String fullName;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer loyaltyPoints;

    private LocalDate joinDate;
}
