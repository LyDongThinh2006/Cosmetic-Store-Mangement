package com.thinh.cosmetic.rest.purchase;

import com.thinh.cosmetic.domain.dto.request.purchase.SupplierRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.SupplierResponse;
import com.thinh.cosmetic.service.purchase.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
public class SupplierRestController {
    private final SupplierService supplierService;

    @PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<SupplierResponse> create(@Valid @RequestBody SupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(request));
    }

    @PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    @PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request
    ) throws Exception {
        return ResponseEntity.ok(supplierService.update(id, request));
    }

    @PreAuthorize("hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) throws Exception {
        supplierService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
