package com.thinh.cosmetic.rest.store;

import com.thinh.cosmetic.domain.dto.request.store.InventoryAdjustmentRequest;
import com.thinh.cosmetic.domain.dto.response.store.InventoryResponse;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.store.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryRestController {
    private final InventoryService inventoryService;

    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryResponse>> getByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryService.getByStore(storeId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping("/store/{storeId}/sku/{skuId}")
    public ResponseEntity<InventoryResponse> getByStoreAndSku(
            @PathVariable Long storeId,
            @PathVariable Long skuId
    ) throws Exception {
        return ResponseEntity.ok(inventoryService.getByStoreAndSku(storeId, skuId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping("/store/{storeId}/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockItems(@PathVariable Long storeId) {
        return ResponseEntity.ok(inventoryService.getLowStockItems(storeId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_ADJUST')")
    @PostMapping("/adjust")
    public ResponseEntity<InventoryResponse> adjustStock(
            @Valid @RequestBody InventoryAdjustmentRequest request,
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) Long employeeId
    ) throws Exception {
        Long empId = (principal != null && principal.getEmployeeId() != null)
                ? principal.getEmployeeId()
                : employeeId;
        if (empId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được mã nhân viên điều chỉnh kho");
        }
        return ResponseEntity.ok(inventoryService.adjustStock(request, empId));
    }

    @PreAuthorize("hasAuthority('INVENTORY_VIEW')")
    @GetMapping("/store/{storeId}/sku/{skuId}/available")
    public ResponseEntity<Integer> getAvailableStock(
            @PathVariable Long storeId,
            @PathVariable Long skuId
    ) {
        return ResponseEntity.ok(inventoryService.getAvailableStock(storeId, skuId));
    }
}
