package com.thinh.cosmetic.rest.order;

import com.thinh.cosmetic.domain.dto.request.promotion.VoucherRequest;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherResponse;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherValidationResponse;
import com.thinh.cosmetic.service.order.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
public class VoucherRestController {

    private final VoucherService voucherService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<List<VoucherResponse>> getAll() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    @GetMapping("/active")
    public ResponseEntity<List<VoucherResponse>> getActiveVouchers() {
        return ResponseEntity.ok(voucherService.getActiveVouchers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<VoucherResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(voucherService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<VoucherResponse> create(@Valid @RequestBody VoucherRequest request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(voucherService.createVoucher(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<VoucherResponse> update(@PathVariable Long id, @Valid @RequestBody VoucherRequest request) throws Exception {
        return ResponseEntity.ok(voucherService.updateVoucher(id, request));
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    public ResponseEntity<VoucherResponse> toggleStatus(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(voucherService.toggleStatus(id));
    }

    @PostMapping("/validate")
    public ResponseEntity<VoucherValidationResponse> validateVoucher(@RequestBody Map<String, Object> payload) {
        String code = payload.get("code") != null ? payload.get("code").toString() : "";
        BigDecimal subtotal = payload.get("subtotal") != null
                ? new BigDecimal(payload.get("subtotal").toString())
                : BigDecimal.ZERO;
        BigDecimal shippingFee = payload.get("shippingFee") != null
                ? new BigDecimal(payload.get("shippingFee").toString())
                : new BigDecimal("30000");

        return ResponseEntity.ok(voucherService.validateVoucher(code, subtotal, shippingFee));
    }
}
