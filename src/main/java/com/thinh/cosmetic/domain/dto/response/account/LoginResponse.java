package com.thinh.cosmetic.domain.dto.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thinh.cosmetic.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private AccountType accountType;
    private String displayName;
    private String redirectUrl;
    private Instant expiresAt;

    @JsonProperty("isEmployee")
    public boolean isEmployee() {
        return accountType == AccountType.EMPLOYEE;
    }
}
