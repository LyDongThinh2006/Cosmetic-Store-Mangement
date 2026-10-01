package com.thinh.cosmetic.domain.dto.response.account;

import com.thinh.cosmetic.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AccountResponse {
    private Long id;
    private String email;
    private String phone;
    private AccountType accountType;
    private LocalDateTime createdAt;
}
