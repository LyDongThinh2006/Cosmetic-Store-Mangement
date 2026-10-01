package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.domain.dto.request.account.EmployeeRequest;
import com.thinh.cosmetic.domain.dto.response.account.EmployeeResponse;
import com.thinh.cosmetic.domain.entity.account.*;
import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.*;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.service.account.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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

    @Override
    public EmployeeResponse create(EmployeeRequest request) throws Exception {
        if (accountRepository.existsByEmail(request.getInternalEmail())) {
            throw new Exception("Email already exists: " + request.getInternalEmail());
        }

        AccountEntity account = AccountEntity.builder()
                .username(request.getInternalEmail())
                .email(request.getInternalEmail())
                .phone(request.getPhone())
                .passwordHash("123456") // default password
                .accountType(AccountType.EMPLOYEE)
                .build();
        account = accountRepository.save(account);

        EmployeeEntity employee = EmployeeEntity.builder()
                .account(account)
                .fullName(request.getFullName())
                .internalEmail(request.getInternalEmail())
                .phone(request.getPhone())
                .status(ActiveStatus.ACTIVE)
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
            for (Long storeId : request.getStoreIds()) {
                StoreEntity store = storeRepository.findById(storeId).orElse(null);
                if (store != null) {
                    employeeStoreRepository.save(EmployeeStoreEntity.builder()
                            .employee(employee).store(store).build());
                }
            }
        }

        return toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getById(Long id) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new Exception("Employee not found: " + id));
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
                .orElseThrow(() -> new Exception("Employee not found: " + id));
        if (request.getFullName() != null) employee.setFullName(request.getFullName());
        if (request.getPhone() != null) employee.setPhone(request.getPhone());

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
            employeeStoreRepository.deleteById(employee.getId());
            for (Long storeId : request.getStoreIds()) {
                StoreEntity store = storeRepository.findById(storeId).orElse(null);
                if (store != null) {
                    employeeStoreRepository.save(EmployeeStoreEntity.builder()
                            .employee(employee).store(store).build());
                }
            }
        }

        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public void deactivate(Long id) throws Exception {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new Exception("Employee not found: " + id));
        employee.setStatus(ActiveStatus.INACTIVE);
        employeeRepository.save(employee);
    }

    private EmployeeResponse toResponse(EmployeeEntity e) {
        List<String> roles = employeeRoleRepository.findByEmployeeId(e.getId())
                .stream().map(r -> r.getRole().getName()).toList();
        List<String> stores = employeeStoreRepository.findById(e.getId())
                .stream().map(s -> s.getStore().getName()).toList();

        return EmployeeResponse.builder()
                .id(e.getId())
                .accountId(e.getAccount() != null ? e.getAccount().getId() : null)
                .email(e.getAccount() != null ? e.getAccount().getEmail() : null)
                .fullName(e.getFullName())
                .internalEmail(e.getInternalEmail())
                .phone(e.getPhone())
                .status(e.getStatus())
                .roles(roles)
                .stores(stores)
                .build();
    }
}
