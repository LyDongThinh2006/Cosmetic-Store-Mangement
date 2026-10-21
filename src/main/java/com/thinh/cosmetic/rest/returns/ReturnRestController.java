package com.thinh.cosmetic.rest.returns;

import com.thinh.cosmetic.domain.dto.request.returns.ReturnRequestDto;
import com.thinh.cosmetic.domain.dto.response.returns.ReturnRequestResponse;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.returns.ReturnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@RequiredArgsConstructor
public class ReturnRestController {
    private final ReturnService returnService;

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<ReturnRequestResponse> create(
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody ReturnRequestDto request
    ) throws Exception {
        Long customerId = (principal != null) ? principal.getCustomerId() : null;
        if (customerId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng từ phiên đăng nhập");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(returnService.create(customerId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnRequestResponse> getById(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long id
    ) throws Exception {
        ReturnRequestResponse response = returnService.getById(id);
        if (principal != null && principal.isCustomer()) {
            if (response.getCustomerId() != null && !response.getCustomerId().equals(principal.getCustomerId())) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không có quyền xem yêu cầu trả hàng của tài khoản khác");
            }
        }
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ADMIN', 'ORDER_PROCESS', 'CUSTOMER_MANAGE')")
    @GetMapping
    public ResponseEntity<List<ReturnRequestResponse>> getAll() {
        return ResponseEntity.ok(returnService.getAll());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReturnRequestResponse>> getByCustomer(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long customerId
    ) {
        if (principal != null && principal.isCustomer() && !customerId.equals(principal.getCustomerId())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED,
                    "Không có quyền xem yêu cầu trả hàng của tài khoản khác");
        }
        return ResponseEntity.ok(returnService.getByCustomer(customerId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ReturnRequestResponse>> getByOrder(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long orderId
    ) {
        List<ReturnRequestResponse> list = returnService.getByOrder(orderId);
        if (principal != null && principal.isCustomer() && !list.isEmpty()) {
            if (list.get(0).getCustomerId() != null && !principal.getCustomerId().equals(list.get(0).getCustomerId())) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Không có quyền xem yêu cầu trả hàng của đơn hàng này");
            }
        }
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN', 'ORDER_PROCESS', 'CUSTOMER_MANAGE')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ReturnRequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ReturnStatus status,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String note,
            @RequestParam(required = false, defaultValue = "true") Boolean restock
    ) throws Exception {
        Long empId = (principal != null && principal.getEmployeeId() != null)
                ? principal.getEmployeeId()
                : employeeId;
        if (empId == null) {
            empId = 1L;
        }
        return ResponseEntity.ok(returnService.updateStatus(id, status, empId, note, restock));
    }
}
