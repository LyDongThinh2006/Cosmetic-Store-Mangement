package com.thinh.cosmetic.security;

import com.thinh.cosmetic.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPrincipal implements Serializable {
    private Long accountId;
    private Long customerId;
    private Long employeeId;
    private String displayName;
    private String username;
    private AccountType accountType;
    private Collection<? extends GrantedAuthority> authorities;

    public boolean isCustomer() {
        return accountType == AccountType.CUSTOMER;
    }

    public boolean isEmployee() {
        return accountType == AccountType.EMPLOYEE;
    }
}
