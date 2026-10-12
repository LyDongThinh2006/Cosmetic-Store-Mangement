package com.thinh.cosmetic.service;

import com.thinh.cosmetic.config.AppProperties;
import com.thinh.cosmetic.domain.dto.request.account.ResetPasswordRequest;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.mapper.account.CustomerMapper;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.security.JwtService;
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

import java.util.Optional;

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
    @DisplayName("UC03: Reset password successfully updates hashed password")
    void testResetPasswordSuccess() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase("khach01@lunea.test", "khach01@lunea.test", "khach01@lunea.test"))
                .thenReturn(Optional.of(account));
        when(passwordEncoder.encode("newPassword123")).thenReturn("new_hashed_password");
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(i -> i.getArgument(0));

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailOrPhone("khach01@lunea.test")
                .newPassword("newPassword123")
                .build();

        assertDoesNotThrow(() -> accountService.resetPassword(request));
        assertEquals("new_hashed_password", account.getPasswordHash());
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("UC03: Reset password throws RESOURCE_NOT_FOUND if user does not exist")
    void testResetPasswordUserNotFound() {
        when(accountRepository.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase("unknown@lunea.test", "unknown@lunea.test", "unknown@lunea.test"))
                .thenReturn(Optional.empty());

        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailOrPhone("unknown@lunea.test")
                .newPassword("newPassword123")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> accountService.resetPassword(request));
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND, exception.getErrorCode());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("UC03: Reset password throws VALIDATION_ERROR if emailOrPhone is blank")
    void testResetPasswordBlankIdentifier() {
        ResetPasswordRequest request = ResetPasswordRequest.builder()
                .emailOrPhone("   ")
                .newPassword("newPassword123")
                .build();

        BusinessException exception = assertThrows(BusinessException.class, () -> accountService.resetPassword(request));
        assertEquals(ErrorCode.VALIDATION_ERROR, exception.getErrorCode());
        verify(accountRepository, never()).save(any());
    }
}
