package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.config.AppProperties;
import com.thinh.cosmetic.domain.dto.request.account.LoginRequest;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.dto.response.account.LoginResponse;
import com.thinh.cosmetic.domain.dto.response.account.LoginResult;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.mapper.account.CustomerMapper;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.security.AppUserDetails;
import com.thinh.cosmetic.security.JwtService;
import com.thinh.cosmetic.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppProperties appProperties;

    @Override
    public CustomerResponse register(RegisterRequest request) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String phone = request.getPhone() != null ? request.getPhone().replaceAll("\\s+", "").replace("+84", "0") : null;
        String fullName = request.getFullName() != null ? request.getFullName().trim() : "";

        if (accountRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "Email already exists: " + email);
        }

        if (phone != null && !phone.isBlank() && accountRepository.existsByPhone(phone)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PHONE, "Phone number already exists: " + phone);
        }

        AccountEntity account = AccountEntity.builder()
                .username(email)
                .email(email)
                .phone(phone != null && !phone.isBlank() ? phone : null)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .accountType(AccountType.CUSTOMER)
                .status(ActiveStatus.ACTIVE)
                .build();
        account = accountRepository.save(account);

        CustomerEntity customer = CustomerEntity.builder()
                .account(account)
                .fullName(fullName)
                .loyaltyPoints(0)
                .joinDate(LocalDate.now())
                .build();
        customer = customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResult login(LoginRequest request) {
        String identifier = request.getLoginIdentifier();

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
            );
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa");
        }

        if (!(authentication.getPrincipal() instanceof AppUserDetails userDetails)) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa");
        }

        if (userDetails.getAccount() != null && userDetails.getAccount().getStatus() == ActiveStatus.INACTIVE) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS, "Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa");
        }

        String token = jwtService.issueAccessToken(userDetails);
        long accessTtl = appProperties.getJwt().getAccessTtl();
        Instant expiresAt = Instant.now().plusSeconds(accessTtl);

        String redirectUrl = userDetails.getAccountType() == AccountType.EMPLOYEE ? "/admin" : "/";

        LoginResponse loginResponse = LoginResponse.builder()
                .accountType(userDetails.getAccountType())
                .displayName(userDetails.getDisplayName())
                .redirectUrl(redirectUrl)
                .expiresAt(expiresAt)
                .build();

        return LoginResult.builder()
                .response(loginResponse)
                .token(token)
                .build();
    }
}
