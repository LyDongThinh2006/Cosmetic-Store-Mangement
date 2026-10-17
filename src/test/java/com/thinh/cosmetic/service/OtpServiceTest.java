package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.PasswordResetOtpEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.PasswordResetOtpRepository;
import com.thinh.cosmetic.service.account.impl.OtpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.thinh.cosmetic.service.notification.EmailService;

@ExtendWith(MockitoExtension.class)
class OtpServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordResetOtpRepository passwordResetOtpRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OtpServiceImpl otpService;

    private AccountEntity activeAccount;
    private PasswordResetOtpEntity validOtpEntity;

    @BeforeEach
    void setUp() {
        activeAccount = AccountEntity.builder()
                .id(10L)
                .username("testuser@lunea.test")
                .email("testuser@lunea.test")
                .phone("0987654321")
                .passwordHash("hashed_old_pwd")
                .accountType(AccountType.CUSTOMER)
                .status(ActiveStatus.ACTIVE)
                .build();

        validOtpEntity = PasswordResetOtpEntity.builder()
                .id(1L)
                .account(activeAccount)
                .identifier("testuser@lunea.test")
                .otpCode("654321")
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .failedAttempts(0)
                .createdAt(LocalDateTime.now().minusMinutes(2))
                .build();
    }

    @Test
    @DisplayName("Gửi OTP thành công khi tài khoản tồn tại và không bị cooldown")
    void testSendOtpSuccess() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(eq("testuser@lunea.test"), any(), eq("testuser@lunea.test")))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.empty());
        when(passwordResetOtpRepository.findByAccountAndUsedFalse(activeAccount))
                .thenReturn(Collections.emptyList());
        when(passwordResetOtpRepository.save(any(PasswordResetOtpEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        SendOtpRequest request = SendOtpRequest.builder().emailOrPhone("testuser@lunea.test").build();
        SendOtpResponse response = otpService.sendPasswordResetOtp(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(300, response.getExpiresInSeconds());
        assertNotNull(response.getDebugOtp());
        assertEquals(6, response.getDebugOtp().length());
        assertTrue(response.getMaskedDestination().contains("***"));
        verify(passwordResetOtpRepository).save(any(PasswordResetOtpEntity.class));
        verify(emailService).sendOtpEmail(eq("testuser@lunea.test"), any(), eq(5));
    }

    @Test
    @DisplayName("Gửi OTP thất bại khi không tìm thấy tài khoản")
    void testSendOtpUserNotFound() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.empty());

        SendOtpRequest request = SendOtpRequest.builder().emailOrPhone("unknown@lunea.test").build();
        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.sendPasswordResetOtp(request));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("Gửi OTP thất bại khi tài khoản đã bị khóa / INACTIVE")
    void testSendOtpInactiveAccount() {
        activeAccount.setStatus(ActiveStatus.INACTIVE);
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.of(activeAccount));

        SendOtpRequest request = SendOtpRequest.builder().emailOrPhone("testuser@lunea.test").build();
        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.sendPasswordResetOtp(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
    }

    @Test
    @DisplayName("Gửi OTP thất bại do vi phạm cooldown 60 giây")
    void testSendOtpCooldownActive() {
        PasswordResetOtpEntity recentOtp = PasswordResetOtpEntity.builder()
                .account(activeAccount)
                .createdAt(LocalDateTime.now().minusSeconds(20))
                .build();

        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(recentOtp));

        SendOtpRequest request = SendOtpRequest.builder().emailOrPhone("testuser@lunea.test").build();
        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.sendPasswordResetOtp(request));
        assertEquals(ErrorCode.OTP_COOLDOWN, ex.getErrorCode());
    }

    @Test
    @DisplayName("Xác thực OTP thành công với mã khớp và còn hạn")
    void testVerifyOtpSuccess() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(eq("testuser@lunea.test"), any(), eq("testuser@lunea.test")))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(validOtpEntity));

        VerifyOtpRequest request = VerifyOtpRequest.builder()
                .emailOrPhone("testuser@lunea.test")
                .otp("654321")
                .build();

        PasswordResetOtpEntity verified = otpService.verifyOtp(request);
        assertNotNull(verified);
        assertEquals("654321", verified.getOtpCode());
    }

    @Test
    @DisplayName("Xác thực OTP thất bại khi mã OTP không đúng, tăng số lần thử sai")
    void testVerifyOtpWrongCode() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(validOtpEntity));

        VerifyOtpRequest request = VerifyOtpRequest.builder()
                .emailOrPhone("testuser@lunea.test")
                .otp("000000")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.verifyOtp(request));
        assertEquals(ErrorCode.INVALID_OTP, ex.getErrorCode());
        assertEquals(1, validOtpEntity.getFailedAttempts());
        verify(passwordResetOtpRepository).save(validOtpEntity);
    }

    @Test
    @DisplayName("Xác thực OTP thất bại khi mã đã hết hạn")
    void testVerifyOtpExpired() {
        validOtpEntity.setExpiryTime(LocalDateTime.now().minusMinutes(1));

        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(validOtpEntity));

        VerifyOtpRequest request = VerifyOtpRequest.builder()
                .emailOrPhone("testuser@lunea.test")
                .otp("654321")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.verifyOtp(request));
        assertEquals(ErrorCode.OTP_EXPIRED, ex.getErrorCode());
        assertTrue(validOtpEntity.isUsed());
    }

    @Test
    @DisplayName("Xác thực OTP thất bại khi nhập sai quá 5 lần")
    void testVerifyOtpMaxAttempts() {
        validOtpEntity.setFailedAttempts(5);

        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(validOtpEntity));

        VerifyOtpRequest request = VerifyOtpRequest.builder()
                .emailOrPhone("testuser@lunea.test")
                .otp("654321")
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> otpService.verifyOtp(request));
        assertEquals(ErrorCode.OTP_MAX_ATTEMPTS, ex.getErrorCode());
        assertTrue(validOtpEntity.isUsed());
    }

    @Test
    @DisplayName("Đổi mật khẩu hoàn tất thành công: hash password và đánh dấu OTP đã dùng")
    void testCompletePasswordResetSuccess() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(eq("testuser@lunea.test"), any(), eq("testuser@lunea.test")))
                .thenReturn(Optional.of(activeAccount));
        when(passwordResetOtpRepository.findTopByAccountAndUsedFalseOrderByCreatedAtDesc(activeAccount))
                .thenReturn(Optional.of(validOtpEntity));
        when(passwordEncoder.encode("newSecretPass")).thenReturn("encoded_new_secret");

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailOrPhone("testuser@lunea.test")
                .otp("654321")
                .newPassword("newSecretPass")
                .build();

        assertDoesNotThrow(() -> otpService.completePasswordReset(request));
        assertEquals("encoded_new_secret", activeAccount.getPasswordHash());
        assertTrue(validOtpEntity.isUsed());
        verify(accountRepository).save(activeAccount);
        verify(passwordResetOtpRepository).save(validOtpEntity);
    }

    @Test
    @DisplayName("Kiểm tra định dạng che giấu (masking) Email và SĐT")
    void testMaskIdentifier() {
        assertEquals("te***r@lunea.test", otpService.maskIdentifier("testuser@lunea.test"));
        assertEquals("098****321", otpService.maskIdentifier("0987654321"));
        assertEquals("***", otpService.maskIdentifier(""));
    }
}
