package com.thinh.cosmetic.rest.review;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.enums.ReviewStatus;
import com.thinh.cosmetic.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
            @RequestParam(required = false, defaultValue = "1") Long customerId,
            @Valid @RequestBody ReviewRequest request
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(customerId, request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getByProduct(productId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReviewResponse> updateModerationStatus(
            @PathVariable Long id,
            @RequestParam ReviewStatus status
    ) throws Exception {
        return ResponseEntity.ok(reviewService.updateModerationStatus(id, status));
    }
}
