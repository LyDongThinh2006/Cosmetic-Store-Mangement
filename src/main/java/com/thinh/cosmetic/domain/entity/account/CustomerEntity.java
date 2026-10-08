package com.thinh.cosmetic.domain.entity.account;

import com.thinh.cosmetic.domain.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Account cannot be null")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private AccountEntity account;

    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "birth_date")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @NotNull(message = "Loyalty points cannot be null")
    @Min(value = 0, message = "Loyalty points must be greater than or equal to 0")
    @Column(name = "loyalty_points", nullable = false)
    @Builder.Default
    private Integer loyaltyPoints = 0;

    @Column(name = "join_date")
    private LocalDate joinDate;
}
