package com.thinh.cosmetic.service.review.impl;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.entity.review.ReviewEntity;
import com.thinh.cosmetic.domain.enums.ReviewModerationStatus;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.review.ReviewRepository;
import com.thinh.cosmetic.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public ReviewResponse create(Long customerId, ReviewRequest request) throws Exception {
        ReviewEntity review = ReviewEntity.builder()
                .product(productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new Exception("Product not found: " + request.getProductId())))
                .customer(customerRepository.findById(customerId)
                        .orElseThrow(() -> new Exception("Customer not found: " + customerId)))
                .order(request.getOrderId() != null ? orderRepository.findById(request.getOrderId()).orElse(null) : null)
                .rating(request.getRating())
                .comment(request.getComment())
                .moderationStatus(ReviewModerationStatus.VISIBLE)
                .build();
        return toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getByProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .filter(r -> r.getModerationStatus() == ReviewModerationStatus.VISIBLE)
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAll() {
        return reviewRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public ReviewResponse updateModerationStatus(Long id, ReviewModerationStatus status) throws Exception {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new Exception("Review not found: " + id));
        review.setModerationStatus(status);
        return toResponse(reviewRepository.save(review));
    }

    private ReviewResponse toResponse(ReviewEntity e) {
        return ReviewResponse.builder()
                .id(e.getId())
                .productId(e.getProduct().getId())
                .productName(e.getProduct().getName())
                .customerName(e.getCustomer() != null ? e.getCustomer().getFullName() : null)
                .rating(e.getRating())
                .comment(e.getComment())
                .moderationStatus(e.getModerationStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
