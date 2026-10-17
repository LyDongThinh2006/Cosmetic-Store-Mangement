package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.PasswordResetOtpEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.PasswordResetOtpRepository;
import com.thinh.cosmetic.service.account.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinh.cosmetic.service.notification.EmailService;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int OTP_COOLDOWN_SECONDS = 60;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final AccountRepository accountRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public SendOtpResponse sendPasswordResetOtp(SendOtpRequest request) {
        String identifier = request.getEmailOrPhone() != null ? request.getEmailOrPhone().trim() : "";
        if (identifier.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Email hoặc số điện thoại không được để trống");
        }

        String normalizedPhone = identifier.replaceAll("\\s+", "").replace("+84", "0");
        AccountEntity account = accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(identifier, normalizedPhone, identifier)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy tài khoản tương ứng với thông tin đã nhập"));

        if (account.getStatus() == ActiveStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Tài khoản này đã bị khóa hoặc ngừng hoạt động");
        }

        // Check 60-second cooldown from the most recently generated OTP
        Optional<PasswordResetOtpEntity> lastOtpOpt = passwordResetOtpRepository.findTopByAccountOrderByCreatedAtDesc(account);
        if (lastOtpOpt.isPresent() && lastOtpOpt.get().getCreatedAt() != null) {
            LocalDateTime createdAt = lastOtpOpt.get().getCreatedAt();
            LocalDateTime cooldownUntil = createdAt.plusSeconds(OTP_COOLDOWN_SECONDS);
            if (cooldownUntil.isAfter(LocalDateTime.now())) {
                long waitSeconds = Duration.between(LocalDateTime.now(), cooldownUntil).getSeconds() + 1;
                throw new BusinessException(
                        ErrorCode.OTP_COOLDOWN,
                        "Vui lòng đợi " + Math.max(1, waitSeconds) + " giây trước khi yêu cầu gửi lại mã OTP mới."
                );
            }
        }

        // Invalidate any existing unused OTPs for this account
        List<PasswordResetOtpEntity> activeOtps = passwordResetOtpRepository.findByAccountAndUsedFalse(account);
        for (PasswordResetOtpEntity oldOtp : activeOtps) {
            oldOtp.setUsed(true);
        }
        passwordResetOtpRepository.saveAll(activeOtps);

        // Generate cryptographically secure 6-digit OTP (100000 - 999999)
        int code = 100000 + secureRandom.nextInt(900000);
        String otpCode = String.valueOf(code);

        PasswordResetOtpEntity otpEntity = PasswordResetOtpEntity.builder()
                .account(account)
                .identifier(identifier)
                .otpCode(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES))
                .used(false)
                .failedAttempts(0)
                .build();

        passwordResetOtpRepository.save(otpEntity);

        String destination = account.getEmail() != null && !account.getEmail().isBlank()
                ? account.getEmail()
                : (account.getPhone() != null ? account.getPhone() : identifier);
        String maskedDestination = maskIdentifier(destination);

        // Send branded OTP email if destination is an email address
        if (destination.contains("@")) {
            emailService.sendOtpEmail(destination, otpCode, OTP_EXPIRY_MINUTES);
        }

        log.info("=================================================================");
        log.info(">>> [LUNEA OTP] MÃ OTP KHÔI PHỤC MẬT KHẨU CHO [{}]: {}", destination, otpCode);
        log.info(">>> [LUNEA OTP] Tài khoản: {} | Hiệu lực: {} phút | Hết hạn: {}",
                account.getUsername(), OTP_EXPIRY_MINUTES, otpEntity.getExpiryTime());
        log.info("=================================================================");

        return SendOtpResponse.builder()
                .success(true)
                .message("Mã xác thực OTP đã được gửi thành công đến " + maskedDestination + ". Mã có hiệu lực trong " + OTP_EXPIRY_MINUTES + " phút.")
                .emailOrPhone(identifier)
                .maskedDestination(maskedDestination)
                .expiresInSeconds(OTP_EXPIRY_MINUTES * 60L)
                .debugOtp(otpCode)
                .build();
    }

    @Override
    public PasswordResetOtpEntity verifyOtp(VerifyOtpRequest request) {
        String identifier = request.getEmailOrPhone() != null ? request.getEmailOrPhone().trim() : "";
        String otp = request.getOtp() != null ? request.getOtp().trim() : "";

        if (identifier.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Email hoặc số điện thoại không được để trống");
        }
        if (otp.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mã OTP không được để trống");
        }

        String normalizedPhone = identifier.replaceAll("\\s+", "").replace("+84", "0");
        AccountEntity account = accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(identifier, normalizedPhone, identifier)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy tài khoản tương ứng với thông tin đã nhập"));

        PasswordResetOtpEntity otpEntity = passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(account)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_OTP, "Không tìm thấy mã OTP hợp lệ hoặc mã đã được sử dụng. Vui lòng yêu cầu mã mới."));

        if (otpEntity.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            otpEntity.setUsed(true);
            passwordResetOtpRepository.save(otpEntity);
            throw new BusinessException(ErrorCode.OTP_MAX_ATTEMPTS, "Bạn đã nhập sai mã OTP quá " + MAX_FAILED_ATTEMPTS + " lần. Vui lòng yêu cầu mã mới.");
        }

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpEntity.setUsed(true);
            passwordResetOtpRepository.save(otpEntity);
            throw new BusinessException(ErrorCode.OTP_EXPIRED, "Mã OTP đã hết hạn. Vui lòng yêu cầu gửi lại mã mới.");
        }

        if (!otpEntity.getOtpCode().equals(otp)) {
            otpEntity.setFailedAttempts(otpEntity.getFailedAttempts() + 1);
            passwordResetOtpRepository.save(otpEntity);
            int remaining = MAX_FAILED_ATTEMPTS - otpEntity.getFailedAttempts();
            if (remaining <= 0) {
                otpEntity.setUsed(true);
                passwordResetOtpRepository.save(otpEntity);
                throw new BusinessException(ErrorCode.OTP_MAX_ATTEMPTS, "Bạn đã nhập sai mã OTP quá " + MAX_FAILED_ATTEMPTS + " lần. Vui lòng yêu cầu mã mới.");
            }
            throw new BusinessException(ErrorCode.INVALID_OTP, "Mã OTP không chính xác. Bạn còn " + remaining + " lần thử.");
        }

        return otpEntity;
    }

    @Override
    public void completePasswordReset(ResetPasswordRequest request) {
        String identifier = request.getEmailOrPhone() != null ? request.getEmailOrPhone().trim() : "";
        String newPassword = request.getNewPassword() != null ? request.getNewPassword() : "";

        if (newPassword.isBlank() || newPassword.length() < 6) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mật khẩu mới phải có ít nhất 6 ký tự");
        }

        // Verify OTP (throws exception if invalid, expired, or locked)
        VerifyOtpRequest verifyRequest = VerifyOtpRequest.builder()
                .emailOrPhone(identifier)
                .otp(request.getOtp())
                .build();
        PasswordResetOtpEntity otpEntity = verifyOtp(verifyRequest);

        // Update password
        AccountEntity account = otpEntity.getAccount();
        account.setPasswordHash(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        // Mark OTP as used
        otpEntity.setUsed(true);
        passwordResetOtpRepository.save(otpEntity);

        log.info(">>> [LUNEA OTP] Đặt lại mật khẩu thành công cho tài khoản: {}", account.getUsername());
    }

    @Override
    public String maskIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return "***";
        }
        if (identifier.contains("@")) {
            String[] parts = identifier.split("@", 2);
            String name = parts[0];
            String domain = parts.length > 1 ? parts[1] : "";
            if (name.length() <= 2) {
                return name.charAt(0) + "***@" + domain;
            }
            return name.substring(0, 2) + "***" + name.charAt(name.length() - 1) + "@" + domain;
        } else {
            String phone = identifier.replaceAll("\\s+", "");
            if (phone.length() >= 7) {
                return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 3);
            }
            return phone.substring(0, Math.min(2, phone.length())) + "****";
        }
    }
}
