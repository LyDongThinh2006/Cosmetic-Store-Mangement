package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.domain.dto.request.account.LoginRequest;
import com.thinh.cosmetic.domain.dto.request.account.RegisterRequest;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.mapper.account.CustomerMapper;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse register(RegisterRequest request) throws Exception {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already exists: " + request.getEmail());
        }

        AccountEntity account = AccountEntity.builder()
                .username(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .accountType(AccountType.CUSTOMER)
                .build();
        account = accountRepository.save(account);

        CustomerEntity customer = CustomerEntity.builder()
                .account(account)
                .fullName(request.getFullName())
                .loyaltyPoints(0)
                .joinDate(LocalDate.now())
                .build();
        customer = customerRepository.save(customer);

        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse login(LoginRequest request) throws Exception {
        AccountEntity account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            throw new Exception("Invalid email or password");
        }

        CustomerEntity customer = customerRepository.findByAccountId(account.getId())
                .orElseThrow(() -> new Exception("Customer profile not found"));

        return customerMapper.toResponse(customer);
    }
}
