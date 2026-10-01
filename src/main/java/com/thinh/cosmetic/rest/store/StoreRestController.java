package com.thinh.cosmetic.rest.store;

import com.thinh.cosmetic.domain.dto.request.store.StoreRequest;
import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.service.store.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreRestController {
    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> create(@Valid @RequestBody StoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(storeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAll() {
        return ResponseEntity.ok(storeService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequest request
    ) throws Exception {
        return ResponseEntity.ok(storeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) throws Exception {
        storeService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
