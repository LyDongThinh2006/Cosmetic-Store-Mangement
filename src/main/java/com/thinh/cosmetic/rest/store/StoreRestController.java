package com.thinh.cosmetic.rest.store;

import com.thinh.cosmetic.domain.dto.request.store.StoreRequest;
import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.service.store.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreRestController {
    private final StoreService storeService;

    @PreAuthorize("hasAuthority('STORE_MANAGE')")
    @PostMapping
    public ResponseEntity<StoreResponse> create(@Valid @RequestBody StoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(storeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAll(@RequestParam(required = false) com.thinh.cosmetic.domain.enums.ActiveStatus status) {
        if (status == com.thinh.cosmetic.domain.enums.ActiveStatus.ACTIVE) {
            return ResponseEntity.ok(storeService.getActiveStores());
        }
        return ResponseEntity.ok(storeService.getAll());
    }

    @PreAuthorize("hasAuthority('STORE_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequest request
    ) throws Exception {
        return ResponseEntity.ok(storeService.update(id, request));
    }

    @PreAuthorize("hasAuthority('STORE_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) throws Exception {
        storeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('STORE_MANAGE')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) throws Exception {
        storeService.activate(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('STORE_MANAGE')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePut(@PathVariable Long id) throws Exception {
        storeService.deactivate(id);
        return ResponseEntity.ok().build();
    }
}
