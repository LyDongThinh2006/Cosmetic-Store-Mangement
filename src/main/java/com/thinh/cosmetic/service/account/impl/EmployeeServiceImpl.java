package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.domain.dto.request.account.EmployeeRequest;
import com.thinh.cosmetic.domain.dto.response.account.EmployeeResponse;
import com.thinh.cosmetic.domain.entity.account.*;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.*;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.service.account.EmployeeService;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeStoreRepository employeeStoreRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse create(EmployeeRequest request) throws Exception {
        String targetEmail = (request.getInternalEmail() != null && !request.getInternalEmail().isBlank())
                ? request.getInternalEmail().trim()
                : (request.getEmail() != null ? request.getEmail().trim() : null);

        if (targetEmail == null || targetEmail.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Email nhân viên không được để trống");
        }

        if (accountRepository.existsByEmail(targetEmail)) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "Email đã tồn tại: " + targetEmail);
        }

        String rawPassword = (request.getPassword() != null && !request.getPassword().isBlank()) 
                ? request.getPassword() 
                : "123456";

        String username = request.getUsername();
        if (username == null || username.isBlank()) {
            username = targetEmail.contains("@") ? targetEmail.substring(0, targetEmail.indexOf('@')) : targetEmail;
        }
        username = username.trim();
        if (accountRepository.existsByUsername(username)) {
            username = targetEmail;
        }

        AccountEntity account = AccountEntity.builder()
                .username(username)
                .email(targetEmail)
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(rawPassword))
                .accountType(AccountType.EMPLOYEE)
                .status(request.getStatus() != null ? request.getStatus() : ActiveStatus.ACTIVE)
                .build();
        account = accountRepository.save(account);

        EmployeeEntity employee = EmployeeEntity.builder()
                .account(account)
                .fullName(request.getFullName())
                .internalEmail(targetEmail)
                .phone(request.getPhone())
                .status(request.getStatus() != null ? request.getStatus() : ActiveStatus.ACTIVE)
                .build();
        employee = employeeRepository.save(employee);

        if (request.getRoleIds() != null) {
            for (Long roleId : request.getRoleIds()) {
                RoleEntity role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    employeeRoleRepository.save(EmployeeRoleEntity.builder()
                            .employee(employee).role(role).build());
                }
            }
        }

        if (request.getStoreIds() != null) {
            boolean first = true;
            for (Long storeId : request.getStoreIds()) {
                StoreEntity store = storeRepository.findById(storeId).orElse(null);
                if (store != null) {
                    employeeStoreRepository.save(EmployeeStoreEntity.builder()
                            .employee(employee)
                            .store(store)
                            .isPrimary(first)
                            .build());
                    first = false;
                }
            }
        }

        return toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, "Không tìm thấy nhân viên #" + id));
        return toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAll() {
        return employeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, "Không tìm thấy nhân viên #" + id));

        if (request.getFullName() != null) employee.setFullName(request.getFullName());
        if (request.getPhone() != null) employee.setPhone(request.getPhone());

        String newEmail = (request.getInternalEmail() != null && !request.getInternalEmail().isBlank())
                ? request.getInternalEmail().trim()
                : (request.getEmail() != null ? request.getEmail().trim() : null);

        if (newEmail != null && employee.getAccount() != null && !newEmail.equalsIgnoreCase(employee.getAccount().getEmail())) {
            if (accountRepository.existsByEmail(newEmail)) {
                throw new BusinessException(ErrorCode.DUPLICATE_EMAIL, "Email đã tồn tại: " + newEmail);
            }
            employee.setInternalEmail(newEmail);
            employee.getAccount().setEmail(newEmail);
            employee.getAccount().setUsername(newEmail);
            accountRepository.save(employee.getAccount());
        }

        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
            if (employee.getAccount() != null) {
                employee.getAccount().setStatus(request.getStatus());
                accountRepository.save(employee.getAccount());
            }
        }

        if (request.getPassword() != null && !request.getPassword().isBlank() && employee.getAccount() != null) {
            employee.getAccount().setPasswordHash(passwordEncoder.encode(request.getPassword()));
            accountRepository.save(employee.getAccount());
        }

        if (request.getRoleIds() != null) {
            employeeRoleRepository.deleteByEmployeeId(employee.getId());
            for (Long roleId : request.getRoleIds()) {
                RoleEntity role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    employeeRoleRepository.save(EmployeeRoleEntity.builder()
                            .employee(employee).role(role).build());
                }
            }
        }

        if (request.getStoreIds() != null) {
            employeeStoreRepository.deleteByEmployeeId(employee.getId());
            boolean first = true;
            for (Long storeId : request.getStoreIds()) {
                StoreEntity store = storeRepository.findById(storeId).orElse(null);
                if (store != null) {
                    employeeStoreRepository.save(EmployeeStoreEntity.builder()
                            .employee(employee)
                            .store(store)
                            .isPrimary(first)
                            .build());
                    first = false;
                }
            }
        }

        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public void deactivate(Long id) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, "Không tìm thấy nhân viên #" + id));
        employee.setStatus(ActiveStatus.INACTIVE);
        if (employee.getAccount() != null) {
            employee.getAccount().setStatus(ActiveStatus.INACTIVE);
            accountRepository.save(employee.getAccount());
        }
        employeeRepository.save(employee);
    }

    @Override
    public void activate(Long id) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, "Không tìm thấy nhân viên #" + id));
        employee.setStatus(ActiveStatus.ACTIVE);
        if (employee.getAccount() != null) {
            employee.getAccount().setStatus(ActiveStatus.ACTIVE);
            accountRepository.save(employee.getAccount());
        }
        employeeRepository.save(employee);
    }

    private EmployeeResponse toResponse(EmployeeEntity e) {
        List<EmployeeRoleEntity> empRoles = employeeRoleRepository.findByEmployeeId(e.getId());
        List<String> roles = empRoles.stream()
                .filter(r -> r.getRole() != null)
                .map(r -> r.getRole().getName())
                .toList();
        List<Long> roleIds = empRoles.stream()
                .filter(r -> r.getRole() != null && r.getRole().getId() != null)
                .map(r -> r.getRole().getId())
                .toList();

        List<EmployeeStoreEntity> empStores = employeeStoreRepository.findByEmployeeId(e.getId());
        List<String> stores = empStores.stream()
                .filter(s -> s.getStore() != null)
                .map(s -> s.getStore().getName())
                .toList();
        List<Long> storeIds = empStores.stream()
                .filter(s -> s.getStore() != null && s.getStore().getId() != null)
                .map(s -> s.getStore().getId())
                .toList();

        return EmployeeResponse.builder()
                .id(e.getId())
                .accountId(e.getAccount() != null ? e.getAccount().getId() : null)
                .username(e.getAccount() != null ? e.getAccount().getUsername() : null)
                .email(e.getAccount() != null ? e.getAccount().getEmail() : null)
                .fullName(e.getFullName())
                .internalEmail(e.getInternalEmail())
                .phone(e.getPhone())
                .status(e.getStatus())
                .roles(roles)
                .roleIds(roleIds)
                .stores(stores)
                .storeIds(storeIds)
                .build();
    }
}
