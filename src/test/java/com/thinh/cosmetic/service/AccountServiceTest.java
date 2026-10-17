package com.thinh.cosmetic.service;

import com.thinh.cosmetic.config.AppProperties;
import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.mapper.account.CustomerMapper;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.security.JwtService;
import com.thinh.cosmetic.service.account.OtpService;
import com.thinh.cosmetic.service.account.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountEntity account;

    @BeforeEach
    void setUp() {
        account = AccountEntity.builder()
                .id(1L)
                .username("khach01@lunea.test")
                .email("khach01@lunea.test")
                .phone("0901234567")
                .passwordHash("old_hashed_password")
                .accountType(AccountType.CUSTOMER)
                .status(ActiveStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("UC03: Reset password delegates to OtpService")
    void testResetPasswordSuccess() {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailOrPhone("khach01@lunea.test")
                .otp("123456")
                .newPassword("newPassword123")
                .build();

        doNothing().when(otpService).completePasswordReset(request);

        assertDoesNotThrow(() -> accountService.resetPassword(request));
        verify(otpService).completePasswordReset(request);
    }

    @Test
    @DisplayName("UC03: Send OTP delegates to OtpService")
    void testSendResetPasswordOtp() {
        SendOtpRequest request = SendOtpRequest.builder()
                .emailOrPhone("khach01@lunea.test")
                .build();

        SendOtpResponse mockResponse = SendOtpResponse.builder()
                .success(true)
                .message("OTP sent")
                .emailOrPhone("khach01@lunea.test")
                .maskedDestination("kh***1@lunea.test")
                .expiresInSeconds(300)
                .debugOtp("123456")
                .build();

        when(otpService.sendPasswordResetOtp(request)).thenReturn(mockResponse);

        SendOtpResponse response = accountService.sendResetPasswordOtp(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("kh***1@lunea.test", response.getMaskedDestination());
        verify(otpService).sendPasswordResetOtp(request);
    }

    @Test
    @DisplayName("UC03: Verify OTP delegates to OtpService")
    void testVerifyResetPasswordOtp() {
        VerifyOtpRequest request = VerifyOtpRequest.builder()
                .emailOrPhone("khach01@lunea.test")
                .otp("123456")
                .build();

        when(otpService.verifyOtp(request)).thenReturn(null);

        assertDoesNotThrow(() -> accountService.verifyResetPasswordOtp(request));
        verify(otpService).verifyOtp(request);
    }
}
