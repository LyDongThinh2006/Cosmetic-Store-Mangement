package com.thinh.cosmetic.security;

import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.account.EmployeeRoleEntity;
import com.thinh.cosmetic.domain.entity.account.RolePermissionEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.AccountRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.account.EmployeeRoleRepository;
import com.thinh.cosmetic.repository.account.RolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.isBlank()) {
            throw new BadCredentialsException("Identifier cannot be empty");
        }

        String cleanedIdentifier = identifier.trim();
        String normalizedEmail = cleanedIdentifier.toLowerCase();
        String normalizedPhone = cleanedIdentifier.replaceAll("\\s+", "").replace("+84", "0");

        AccountEntity account = accountRepository
                .findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(normalizedEmail, normalizedPhone, cleanedIdentifier)
                .orElseThrow(() -> new BadCredentialsException("Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa"));

        if (account.getStatus() == ActiveStatus.INACTIVE) {
            throw new BadCredentialsException("Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa");
        }

        CustomerEntity customer = null;
        EmployeeEntity employee = null;
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (account.getAccountType() == AccountType.CUSTOMER) {
            customer = customerRepository.findByAccountId(account.getId()).orElse(null);
            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        } else if (account.getAccountType() == AccountType.EMPLOYEE) {
            employee = employeeRepository.findByAccountId(account.getId()).orElse(null);
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

            if (employee != null) {
                List<EmployeeRoleEntity> employeeRoles = employeeRoleRepository.findByEmployeeId(employee.getId());
                for (EmployeeRoleEntity er : employeeRoles) {
                    if (er.getRole() != null && er.getRole().getName() != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + er.getRole().getName()));
                        authorities.add(new SimpleGrantedAuthority(er.getRole().getName()));
                    }
                }

                List<Long> roleIds = employeeRoles.stream()
                        .filter(er -> er.getRole() != null && er.getRole().getId() != null)
                        .map(er -> er.getRole().getId())
                        .toList();

                if (!roleIds.isEmpty()) {
                    List<RolePermissionEntity> rolePerms = rolePermissionRepository.findByRoleIdIn(roleIds);
                    for (RolePermissionEntity rp : rolePerms) {
                        if (rp.getPermission() != null && rp.getPermission().getCode() != null) {
                            authorities.add(new SimpleGrantedAuthority(rp.getPermission().getCode()));
                        }
                    }
                }
            }
        }

        return new AppUserDetails(account, customer, employee, authorities);
    }
}
