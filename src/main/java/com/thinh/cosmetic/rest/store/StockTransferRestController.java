package com.thinh.cosmetic.rest.store;

import com.thinh.cosmetic.domain.dto.request.store.StockTransferRequest;
import com.thinh.cosmetic.domain.dto.response.store.StockTransferResponse;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.store.StockTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transfers")
@RequiredArgsConstructor
public class StockTransferRestController {
    private final StockTransferService stockTransferService;

    @PreAuthorize("hasAuthority('INVENTORY_ADJUST')")
    @PostMapping
    public ResponseEntity<StockTransferResponse> create(
            @Valid @RequestBody StockTransferRequest request,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long employeeId
    ) throws Exception {
        Long empId = (principal != null && principal.getEmployeeId() != null)
                ? principal.getEmployeeId()
                : employeeId;
        if (empId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được mã nhân viên điều chuyển kho");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(stockTransferService.create(request, empId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_VIEW') or hasAuthority('INVENTORY_ADJUST')")
    @GetMapping("/{id}")
    public ResponseEntity<StockTransferResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stockTransferService.getById(id));
    }

    @PreAuthorize("hasAuthority('INVENTORY_VIEW') or hasAuthority('INVENTORY_ADJUST')")
    @GetMapping
    public ResponseEntity<List<StockTransferResponse>> getAll() {
        return ResponseEntity.ok(stockTransferService.getAll());
    }

    @PreAuthorize("hasAuthority('INVENTORY_ADJUST')")
    @PutMapping("/{id}/ship")
    public ResponseEntity<StockTransferResponse> confirmShipment(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stockTransferService.confirmShipment(id));
    }

    @PreAuthorize("hasAuthority('INVENTORY_ADJUST')")
    @PutMapping("/{id}/receive")
    public ResponseEntity<StockTransferResponse> confirmReceipt(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(stockTransferService.confirmReceipt(id));
    }
}
