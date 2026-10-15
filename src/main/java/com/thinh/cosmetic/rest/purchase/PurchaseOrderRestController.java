package com.thinh.cosmetic.rest.purchase;

import com.thinh.cosmetic.domain.dto.request.purchase.PurchaseOrderRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.PurchaseOrderResponse;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.purchase.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('PURCHASE_MANAGE')")
public class PurchaseOrderRestController {
    private final PurchaseOrderService purchaseOrderService;

    @PreAuthorize("hasAuthority('PURCHASE_MANAGE')")
    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> create(
            @Valid @RequestBody PurchaseOrderRequest request,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long employeeId
    ) throws Exception {
        Long empId = (principal != null && principal.getEmployeeId() != null)
                ? principal.getEmployeeId()
                : employeeId;
        if (empId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được mã nhân viên tạo đơn nhập hàng");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.create(request, empId));
    }

    @PreAuthorize("hasAuthority('PURCHASE_MANAGE')")
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(purchaseOrderService.getById(id));
    }

    @PreAuthorize("hasAuthority('PURCHASE_MANAGE')")
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponse>> getAll() {
        return ResponseEntity.ok(purchaseOrderService.getAll());
    }

    @PreAuthorize("hasAuthority('PURCHASE_MANAGE')")
    @PutMapping("/{id}/confirm")
    public ResponseEntity<PurchaseOrderResponse> confirm(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(purchaseOrderService.confirm(id));
    }
}
