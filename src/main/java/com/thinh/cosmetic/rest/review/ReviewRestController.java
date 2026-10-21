package com.thinh.cosmetic.rest.review;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.enums.ReviewStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @AuthenticationPrincipal AuthPrincipal principal,
            @Valid @RequestBody ReviewRequest request
    ) throws Exception {
        if (principal == null || principal.getCustomerId() == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng từ phiên đăng nhập");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(principal.getCustomerId(), request));
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        if (principal == null || principal.getCustomerId() == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng từ phiên đăng nhập");
        }
        return ResponseEntity.ok(reviewService.getByCustomer(principal.getCustomerId()));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getByProduct(productId));
    }


    @PreAuthorize("hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @PreAuthorize("hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_ADMIN', 'ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ReviewResponse> updateModerationStatus(
            @PathVariable Long id,
            @RequestParam ReviewStatus status
    ) throws Exception {
        return ResponseEntity.ok(reviewService.updateModerationStatus(id, status));
    }
}
