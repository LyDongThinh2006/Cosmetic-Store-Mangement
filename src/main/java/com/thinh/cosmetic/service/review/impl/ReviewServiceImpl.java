package com.thinh.cosmetic.service.review.impl;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.review.ReviewEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.ReviewStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
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
    private final OrderItemRepository orderItemRepository;

    @Override
    public ReviewResponse create(Long customerId, ReviewRequest request) throws Exception {
        if (customerId == null) {
            throw new BusinessException(ErrorCode.UNAUTHENTICATED, "Không xác định được tài khoản khách hàng từ phiên đăng nhập");
        }
        if (request == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Dữ liệu đánh giá không được để trống");
        }
        if (request.getProductId() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mã sản phẩm không được để trống");
        }
        if (request.getOrderId() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mã đơn hàng không được để trống khi gửi đánh giá");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Điểm đánh giá phải từ 1 đến 5 sao");
        }

        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND, "Không tìm thấy tài khoản khách hàng #" + customerId));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "Không tìm thấy sản phẩm #" + request.getProductId()));

        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Không tìm thấy đơn hàng #" + request.getOrderId()));

        // 1. Xác nhận quyền của khách hàng: đơn hàng phải thuộc về chính khách hàng đang đăng nhập
        if (order.getCustomer() == null || !order.getCustomer().getId().equals(customerId)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED, "Đơn hàng này không thuộc về tài khoản của bạn");
        }

        // 2. Xác minh điều kiện được phép đánh giá: đơn hàng phải COMPLETED
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION,
                    "Chỉ có thể đánh giá sản phẩm sau khi đơn hàng đã hoàn tất giao hàng (COMPLETED)");
        }

        // 3. Kiểm tra đơn hàng thực sự chứa sản phẩm đang đánh giá
        List<OrderItemEntity> orderItems = orderItemRepository.findByOrderId(order.getId());
        OrderItemEntity matchingItem = orderItems.stream()
                .filter(item -> item.getSku() != null && item.getSku().getProduct() != null
                        && item.getSku().getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_ERROR,
                        String.format("Đơn hàng #%s không chứa sản phẩm '%s'", order.getOrderCode(), product.getName())));

        // 4. Chống gửi trùng lặp đánh giá cho cùng sản phẩm trong 1 đơn hàng
        if (reviewRepository.existsByCustomerIdAndOrderIdAndProductId(customerId, order.getId(), product.getId())) {
            throw new BusinessException(ErrorCode.DATA_CONFLICT,
                    String.format("Bạn đã gửi đánh giá cho sản phẩm '%s' trong đơn hàng #%s rồi",
                            product.getName(), order.getOrderCode()));
        }

        // 5. Để đánh giá ở trạng thái PENDING chờ kiểm duyệt trước khi công khai
        ReviewEntity review = ReviewEntity.builder()
                .customer(customer)
                .product(product)
                .order(order)
                .orderItem(matchingItem)
                .rating(request.getRating())
                .content(request.getComment() != null ? request.getComment().trim() : null)
                .moderationStatus(ReviewStatus.PENDING)
                .build();

        return toResponse(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getByProduct(Long productId) {
        return reviewRepository.findByProductIdAndModerationStatus(productId, ReviewStatus.VISIBLE)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getByCustomer(Long customerId) {
        return reviewRepository.findByCustomerId(customerId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getAll() {
        return reviewRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    @Override
    public ReviewResponse updateModerationStatus(Long id, ReviewStatus status) throws Exception {
        if (status == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Trạng thái kiểm duyệt không được để trống");
        }
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy đánh giá #" + id));
        review.setModerationStatus(status);
        return toResponse(reviewRepository.save(review));
    }

    private ReviewResponse toResponse(ReviewEntity e) {
        return ReviewResponse.builder()
                .id(e.getId())
                .productId(e.getProduct() != null ? e.getProduct().getId() : null)
                .productName(e.getProduct() != null ? e.getProduct().getName() : null)
                .customerId(e.getCustomer() != null ? e.getCustomer().getId() : null)
                .customerName(e.getCustomer() != null ? e.getCustomer().getFullName() : null)
                .orderId(e.getOrder() != null ? e.getOrder().getId() : null)
                .orderCode(e.getOrder() != null ? e.getOrder().getOrderCode() : null)
                .rating(e.getRating())
                .comment(e.getComment())
                .moderationStatus(e.getModerationStatus())
                .createdAt(e.getCreatedAt())
                .build();
    }
}

