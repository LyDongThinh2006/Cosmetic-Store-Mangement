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
import com.thinh.cosmetic.repository.account.PermissionRepository;
import com.thinh.cosmetic.repository.account.RolePermissionRepository;
import com.thinh.cosmetic.repository.account.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AppUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public AppUserDetailsService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            EmployeeRoleRepository employeeRoleRepository,
            RolePermissionRepository rolePermissionRepository,
            @Autowired(required = false) RoleRepository roleRepository,
            @Autowired(required = false) PermissionRepository permissionRepository
    ) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.employeeRoleRepository = employeeRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public AppUserDetailsService(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            EmployeeRoleRepository employeeRoleRepository,
            RolePermissionRepository rolePermissionRepository
    ) {
        this(accountRepository, customerRepository, employeeRepository, employeeRoleRepository, rolePermissionRepository, null, null);
    }

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
            if (employee == null || employee.getStatus() == ActiveStatus.INACTIVE) {
                throw new BadCredentialsException("Email/SĐT hoặc mật khẩu không đúng, hoặc tài khoản đã bị khóa");
            }
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

            if (employee != null) {
                List<EmployeeRoleEntity> employeeRoles = employeeRoleRepository.findByEmployeeId(employee.getId());
                boolean isAdmin = "admin".equalsIgnoreCase(account.getUsername()) || employeeRoles.stream()
                        .anyMatch(er -> er.getRole() != null && "ADMIN".equalsIgnoreCase(er.getRole().getName()));

                if (isAdmin) {
                    // Admin có FULL QUYỀN của tất cả các quản lý và toàn bộ hệ thống
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    authorities.add(new SimpleGrantedAuthority("ADMIN"));
                    authorities.add(new SimpleGrantedAuthority("ALL_STORES"));

                    // Tự động cấp tất cả các vai trò quản lý (Thủ kho, Đơn hàng, Quản lý sản phẩm,...)
                    if (roleRepository != null) {
                        roleRepository.findAll().forEach(r -> {
                            if (r.getName() != null && !r.getName().isBlank()) {
                                String roleName = r.getName().trim().toUpperCase();
                                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
                                authorities.add(new SimpleGrantedAuthority(roleName));
                            }
                        });
                    }

                    // Tự động cấp tất cả các Permissions trong toàn hệ thống
                    if (permissionRepository != null) {
                        permissionRepository.findAll().forEach(p -> {
                            if (p.getCode() != null && !p.getCode().isBlank()) {
                                authorities.add(new SimpleGrantedAuthority(p.getCode().trim().toUpperCase()));
                            }
                        });
                    }
                } else {
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
        }

        return new AppUserDetails(account, customer, employee, authorities);
    }
}
