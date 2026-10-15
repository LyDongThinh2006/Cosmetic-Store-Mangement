package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.review.ReviewRequest;
import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.review.ReviewEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.ReviewStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.review.ReviewRepository;
import com.thinh.cosmetic.service.review.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private CustomerEntity customer;
    private ProductEntity product;
    private OrderEntity order;
    private OrderItemEntity orderItem;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder().id(1L).fullName("Nguyễn Minh Anh").build();
        product = ProductEntity.builder().id(10L).name("Serum Phục Hồi B5").build();

        order = OrderEntity.builder()
                .id(100L)
                .orderCode("LUN-TEST-100")
                .customer(customer)
                .status(OrderStatus.COMPLETED)
                .build();

        ProductSkuEntity sku = ProductSkuEntity.builder().id(50L).product(product).variantName("30ml").build();
        orderItem = OrderItemEntity.builder()
                .id(200L)
                .order(order)
                .sku(sku)
                .snapshotProductName("Serum Phục Hồi B5")
                .quantity(1)
                .build();
    }

    @Test
    @DisplayName("UC25: Đánh giá hợp lệ được tạo ở trạng thái PENDING chờ kiểm duyệt")
    void testCreateReview_Success_PendingModeration() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(100L)).thenReturn(List.of(orderItem));
        when(reviewRepository.existsByCustomerIdAndOrderIdAndProductId(1L, 100L, 10L)).thenReturn(false);

        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(i -> {
            ReviewEntity r = i.getArgument(0);
            r.setId(99L);
            r.setCreatedAt(LocalDateTime.now());
            return r;
        });

        ReviewRequest request = ReviewRequest.builder()
                .productId(10L)
                .orderId(100L)
                .rating(5)
                .comment("Sản phẩm rất tốt, thấm nhanh và da dịu hẳn!")
                .build();

        ReviewResponse response = reviewService.create(1L, request);

        assertNotNull(response);
        assertEquals(99L, response.getId());
        assertEquals(10L, response.getProductId());
        assertEquals("Serum Phục Hồi B5", response.getProductName());
        assertEquals(1L, response.getCustomerId());
        assertEquals(100L, response.getOrderId());
        assertEquals("LUN-TEST-100", response.getOrderCode());
        assertEquals(5, response.getRating());
        assertEquals(ReviewStatus.PENDING, response.getModerationStatus()); // Chờ kiểm duyệt
        verify(reviewRepository).save(any(ReviewEntity.class));
    }

    @Test
    @DisplayName("UC25: Không cho phép đánh giá đơn hàng chưa hoàn tất (Chưa COMPLETED)")
    void testCreateReview_OrderNotCompleted() {
        order.setStatus(OrderStatus.SHIPPING);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        ReviewRequest request = ReviewRequest.builder()
                .productId(10L)
                .orderId(100L)
                .rating(5)
                .comment("Giao hàng nhanh")
                .build();

        assertThrows(BusinessException.class, () -> reviewService.create(1L, request));
    }

    @Test
    @DisplayName("UC25: Không cho phép khách hàng đánh giá đơn hàng của khách khác (ACCESS_DENIED)")
    void testCreateReview_CustomerDoesNotOwnOrder() {
        when(customerRepository.findById(2L)).thenReturn(Optional.of(CustomerEntity.builder().id(2L).build()));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order)); // order belongs to customer 1L

        ReviewRequest request = ReviewRequest.builder()
                .productId(10L)
                .orderId(100L)
                .rating(5)
                .build();

        assertThrows(BusinessException.class, () -> reviewService.create(2L, request));
    }

    @Test
    @DisplayName("UC25: Không cho phép đánh giá sản phẩm không nằm trong đơn hàng")
    void testCreateReview_OrderDoesNotContainProduct() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        // Order contains a different product
        ProductEntity otherProduct = ProductEntity.builder().id(999L).name("Sản phẩm khác").build();
        ProductSkuEntity otherSku = ProductSkuEntity.builder().id(88L).product(otherProduct).build();
        OrderItemEntity otherItem = OrderItemEntity.builder().id(201L).sku(otherSku).order(order).build();
        when(orderItemRepository.findByOrderId(100L)).thenReturn(List.of(otherItem));

        ReviewRequest request = ReviewRequest.builder()
                .productId(10L)
                .orderId(100L)
                .rating(5)
                .build();

        assertThrows(BusinessException.class, () -> reviewService.create(1L, request));
    }

    @Test
    @DisplayName("UC25: Chống spam - Không cho phép đánh giá trùng lặp sản phẩm trong cùng một đơn hàng")
    void testCreateReview_DuplicateReviewPrevented() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(100L)).thenReturn(List.of(orderItem));
        when(reviewRepository.existsByCustomerIdAndOrderIdAndProductId(1L, 100L, 10L)).thenReturn(true);

        ReviewRequest request = ReviewRequest.builder()
                .productId(10L)
                .orderId(100L)
                .rating(5)
                .comment("Đánh giá lần 2")
                .build();

        assertThrows(BusinessException.class, () -> reviewService.create(1L, request));
    }

    @Test
    @DisplayName("UC25: getByProduct chỉ trả về đánh giá VISIBLE đã được kiểm duyệt công khai")
    void testGetByProduct_OnlyReturnsVisible() {
        ReviewEntity visibleReview = ReviewEntity.builder()
                .id(1L)
                .product(product)
                .customer(customer)
                .rating(5)
                .content("Sản phẩm rất tốt")
                .moderationStatus(ReviewStatus.VISIBLE)
                .createdAt(LocalDateTime.now())
                .build();

        when(reviewRepository.findByProductIdAndModerationStatus(10L, ReviewStatus.VISIBLE))
                .thenReturn(List.of(visibleReview));

        List<ReviewResponse> list = reviewService.getByProduct(10L);

        assertEquals(1, list.size());
        assertEquals(ReviewStatus.VISIBLE, list.get(0).getModerationStatus());
        verify(reviewRepository).findByProductIdAndModerationStatus(10L, ReviewStatus.VISIBLE);
    }

    @Test
    @DisplayName("UC25: Nhân viên duyệt đánh giá (PENDING -> VISIBLE)")
    void testUpdateModerationStatus() throws Exception {
        ReviewEntity review = ReviewEntity.builder()
                .id(1L)
                .product(product)
                .customer(customer)
                .rating(5)
                .moderationStatus(ReviewStatus.PENDING)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenAnswer(i -> i.getArgument(0));

        ReviewResponse response = reviewService.updateModerationStatus(1L, ReviewStatus.VISIBLE);

        assertEquals(ReviewStatus.VISIBLE, response.getModerationStatus());
        verify(reviewRepository).save(review);
    }
}
