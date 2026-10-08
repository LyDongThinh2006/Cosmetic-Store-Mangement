package com.thinh.cosmetic.domain.entity.account;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "employees")
public class EmployeeEntity {
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

    @Email(message = "Work email format is invalid")
    @Size(max = 150, message = "Work email must not exceed 150 characters")
    @Column(name = "work_email", length = 150)
    private String internalEmail;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone", length = 20)
    private String phone;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ActiveStatus status = ActiveStatus.ACTIVE;
}
