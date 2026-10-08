package com.thinh.cosmetic.security;

import com.thinh.cosmetic.domain.entity.account.EmployeeStoreEntity;
import com.thinh.cosmetic.repository.account.EmployeeStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreScope {

    private final EmployeeStoreRepository employeeStoreRepository;

    @Transactional(readOnly = true)
    public List<Long> getAccessibleStoreIds(AuthPrincipal principal) {
        if (principal == null || principal.getAuthorities() == null) {
            return List.of();
        }

        boolean hasAllStores = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> "ALL_STORES".equals(a) || "ROLE_ADMIN".equals(a) || "ADMIN".equals(a));

        if (hasAllStores) {
            return null; // Null signifies all stores are accessible
        }

        if (principal.getEmployeeId() == null) {
            return List.of();
        }

        List<EmployeeStoreEntity> employeeStores = employeeStoreRepository.findByEmployeeId(principal.getEmployeeId());
        return employeeStores.stream()
                .filter(es -> es.getStore() != null && es.getStore().getId() != null)
                .map(es -> es.getStore().getId())
                .toList();
    }

    public boolean canAccessStore(AuthPrincipal principal, Long storeId) {
        List<Long> accessibleIds = getAccessibleStoreIds(principal);
        if (accessibleIds == null) {
            return true;
        }
        return storeId != null && accessibleIds.contains(storeId);
    }
}
