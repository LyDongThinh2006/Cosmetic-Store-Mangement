package com.thinh.cosmetic.security;

import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class AppUserDetails implements UserDetails {

    private final AccountEntity account;
    private final CustomerEntity customer;
    private final EmployeeEntity employee;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserDetails(
            AccountEntity account,
            CustomerEntity customer,
            EmployeeEntity employee,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.account = account;
        this.customer = customer;
        this.employee = employee;
        this.authorities = authorities;
    }

    public Long getAccountId() {
        return account != null ? account.getId() : null;
    }

    public Long getCustomerId() {
        return customer != null ? customer.getId() : null;
    }

    public Long getEmployeeId() {
        return employee != null ? employee.getId() : null;
    }

    public AccountType getAccountType() {
        return account != null ? account.getAccountType() : null;
    }

    public String getDisplayName() {
        if (customer != null && customer.getFullName() != null) {
            return customer.getFullName();
        }
        if (employee != null && employee.getFullName() != null) {
            return employee.getFullName();
        }
        return account != null ? account.getUsername() : "";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account != null ? account.getPasswordHash() : "";
    }

    @Override
    public String getUsername() {
        return account != null ? account.getUsername() : "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account != null && account.getStatus() == ActiveStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account != null && account.getStatus() == ActiveStatus.ACTIVE;
    }
}
