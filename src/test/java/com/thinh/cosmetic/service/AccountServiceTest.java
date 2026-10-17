package com.thinh.cosmetic.service;

import com.thinh.cosmetic.config.AppProperties;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.dto.response.account.SendOtpResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
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

    @Test
    @DisplayName("UC01: Register successfully verifies OTP and creates account")
    void testRegisterWithOtpSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .fullName("Nguyễn Văn A")
                .email("nguyenvana@lunea.test")
                .phone("0912345678")
                .password("Password123")
                .otp("654321")
                .build();

        doNothing().when(otpService).completeRegistrationOtp("nguyenvana@lunea.test", "654321");
        when(accountRepository.existsByEmail("nguyenvana@lunea.test")).thenReturn(false);
        when(accountRepository.existsByPhone("0912345678")).thenReturn(false);
        when(passwordEncoder.encode("Password123")).thenReturn("hashed_new_pwd");
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(customerRepository.save(any(CustomerEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(customerMapper.toResponse(any(CustomerEntity.class))).thenReturn(new CustomerResponse());

        CustomerResponse response = accountService.register(request);
        assertNotNull(response);
        verify(otpService).completeRegistrationOtp("nguyenvana@lunea.test", "654321");
        verify(accountRepository).save(any(AccountEntity.class));
        verify(customerRepository).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("UC01: Send Registration OTP delegates to OtpService")
    void testSendRegistrationOtp() {
        SendOtpRequest request = SendOtpRequest.builder()
                .emailOrPhone("newuser@lunea.test")
                .build();

        SendOtpResponse mockResponse = SendOtpResponse.builder()
                .success(true)
                .message("Mã kích hoạt đã được gửi")
                .emailOrPhone("newuser@lunea.test")
                .maskedDestination("ne***r@lunea.test")
                .expiresInSeconds(300)
                .debugOtp("654321")
                .build();

        when(otpService.sendRegistrationOtp(request)).thenReturn(mockResponse);

        SendOtpResponse response = accountService.sendRegistrationOtp(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(otpService).sendRegistrationOtp(request);
    }
}
