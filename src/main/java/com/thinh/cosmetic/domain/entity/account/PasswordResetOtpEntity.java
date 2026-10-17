package com.thinh.cosmetic.domain.entity.account;

import jakarta.persistence.*;
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
@Table(name = "password_reset_otps", indexes = {
        @Index(name = "idx_otp_identifier", columnList = "identifier"),
        @Index(name = "idx_otp_account_id", columnList = "account_id")
})
public class PasswordResetOtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @NotBlank(message = "Identifier cannot be blank")
    @Size(max = 150)
    @Column(name = "identifier", nullable = false, length = 150)
    private String identifier;

    @NotBlank(message = "OTP code cannot be blank")
    @Size(max = 10)
    @Column(name = "otp_code", nullable = false, length = 10)
    private String otpCode;

    @NotNull(message = "Expiry time cannot be null")
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @NotNull
    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private boolean used = false;

    @Column(name = "failed_attempts", nullable = false)
    @Builder.Default
    private int failedAttempts = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
