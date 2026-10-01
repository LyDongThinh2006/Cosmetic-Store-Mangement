package com.thinh.cosmetic.service.review;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.enums.ReviewModerationStatus;

import java.util.List;

public interface ReviewService {
    ReviewResponse create(Long customerId, ReviewRequest request) throws Exception;
    List<ReviewResponse> getByProduct(Long productId);
    List<ReviewResponse> getAll();
    ReviewResponse updateModerationStatus(Long id, ReviewModerationStatus status) throws Exception;
}
