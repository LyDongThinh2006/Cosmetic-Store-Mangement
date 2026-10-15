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
        return ResponseEntity.status(HttpStatus.CREATED).body(returnService.create(request));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<ReturnRequestResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(returnService.getById(id));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
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
    public ResponseEntity<List<ReturnRequestResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(returnService.getByOrder(orderId));
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') and hasAuthority('ORDER_PROCESS')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ReturnRequestResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ReturnStatus status,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long employeeId
    ) throws Exception {
        Long empId = (principal != null && principal.getEmployeeId() != null)
                ? principal.getEmployeeId()
                : employeeId;
        if (empId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được mã nhân viên");
        }
        return ResponseEntity.ok(returnService.updateStatus(id, status, empId));
    }
}
