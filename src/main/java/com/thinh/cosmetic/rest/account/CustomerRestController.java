package com.thinh.cosmetic.rest.account;

import com.thinh.cosmetic.domain.dto.request.account.BeautyProfileRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerProfileRequest;
import com.thinh.cosmetic.domain.dto.response.account.BeautyProfileResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerAddressResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.account.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('CUSTOMER_MANAGE')")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<CustomerResponse> updateProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody CustomerProfileRequest request
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.updateProfile(id, request));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddresses(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.getAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody CustomerAddressRequest request
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addAddress(id, request));
    }

    @PutMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody CustomerAddressRequest request
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.updateAddress(id, addressId, request));
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        customerService.deleteAddress(id, addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/beauty-profile")
    public ResponseEntity<BeautyProfileResponse> getBeautyProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.getBeautyProfile(id));
    }

    @PutMapping("/{id}/beauty-profile")
    public ResponseEntity<BeautyProfileResponse> updateBeautyProfile(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody BeautyProfileRequest request
    ) throws Exception {
        enforceCustomerOwnership(principal, id);
        return ResponseEntity.ok(customerService.updateBeautyProfile(id, request));
    }

    private void enforceCustomerOwnership(AuthPrincipal principal, Long customerId) {
        if (principal == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Vui lòng đăng nhập");
        }
        if (principal.isEmployee()) {
            boolean hasCustomerManage = principal.getAuthorities() != null &&
                    principal.getAuthorities().stream().anyMatch(a -> "CUSTOMER_MANAGE".equals(a.getAuthority()) || "ROLE_ADMIN".equals(a.getAuthority()));
            if (!hasCustomerManage) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED,
                        "Nhân viên không có quyền quản lý khách hàng");
            }
            return;
        }
        if (principal.isCustomer() && !customerId.equals(principal.getCustomerId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED,
                    "Không có quyền truy cập dữ liệu tài khoản khách hàng khác");
        }
    }
}
