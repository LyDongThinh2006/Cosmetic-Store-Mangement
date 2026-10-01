package com.thinh.cosmetic.rest.catalog;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.dto.response.ApiResponse;
import com.thinh.cosmetic.domain.dto.response.catalog.BrandResponse;
import com.thinh.cosmetic.service.catalog.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/brands")
@RequiredArgsConstructor
public class BrandRestController {
    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> create(
            @Valid @RequestBody BrandRequest request
    ) throws Exception {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<BrandResponse>builder()
                                .status(HttpStatus.CREATED.value())
                                .message("Brand created successfully")
                                .data(brandService.create(request))
                                .build()
                );
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getById(
            @PathVariable Long id
    ) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BrandResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Brand retrieved successfully")
                                .data(brandService.getById(id))
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<BrandResponse>>builder()
                                .status(HttpStatus.OK.value())
                                .message("Brands retrieved successfully")
                                .data(brandService.getAll())
                                .build()
                );
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequest request
    ) throws Exception {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ApiResponse.<BrandResponse>builder()
                                .status(HttpStatus.OK.value())
                                .message("Brand updated successfully")
                                .data(brandService.update(id, request))
                                .build()
                );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) throws Exception {
        brandService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.<Void>builder()
                                .status(HttpStatus.NO_CONTENT.value())
                                .message("Brands retrieved successfully")
                                .data(null)
                                .build()
                );
    }
}
