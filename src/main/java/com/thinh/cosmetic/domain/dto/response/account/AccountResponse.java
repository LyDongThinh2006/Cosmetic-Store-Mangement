package com.thinh.cosmetic.domain.dto.response.account;

import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private AccountType accountType;
    private ActiveStatus status;
    private LocalDateTime createdAt;
}
