package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.returns.ReturnRequestDto;
import com.thinh.cosmetic.domain.dto.response.returns.ReturnRequestResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.entity.returns.ReturnItemEntity;
import com.thinh.cosmetic.domain.entity.returns.ReturnRequestEntity;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.returns.ReturnItemRepository;
import com.thinh.cosmetic.repository.returns.ReturnRequestRepository;
import com.thinh.cosmetic.service.returns.impl.ReturnServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnServiceTest {

    @Mock
    private ReturnRequestRepository returnRequestRepository;

    @Mock
    private ReturnItemRepository returnItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ReturnServiceImpl returnService;

    private OrderEntity order;
    private OrderItemEntity orderItem;
    private ReturnRequestEntity returnRequest;

    @BeforeEach
    void setUp() {
        CustomerEntity customer = CustomerEntity.builder().id(1L).fullName("Minh Anh").build();
        order = OrderEntity.builder().id(100L).orderCode("LUN-100").customer(customer).build();

        ProductEntity product = ProductEntity.builder().id(1L).name("Kem Dưỡng").build();
        ProductSkuEntity sku = ProductSkuEntity.builder().id(10L).product(product).variantName("50ml").price(BigDecimal.valueOf(500000)).build();

        orderItem = OrderItemEntity.builder()
                .id(200L)
                .order(order)
                .sku(sku)
                .quantity(1)
                .snapshotPrice(BigDecimal.valueOf(500000))
                .build();

        ReturnItemEntity returnItem = ReturnItemEntity.builder()
                .id(300L)
                .orderItem(orderItem)
                .quantity(1)
                .detailReason("Hàng bị móp méo khi nhận")
                .build();

        List<ReturnItemEntity> items = new ArrayList<>();
        items.add(returnItem);

        returnRequest = ReturnRequestEntity.builder()
                .id(50L)
                .order(order)
                .reason("DAMAGED_IN_TRANSIT")
                .status(ReturnStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .items(items)
                .build();
    }

    @Test
    @DisplayName("UC26: Create return request creates return entity and items")
    void testCreateReturnRequest() throws Exception {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(returnRequestRepository.save(any(ReturnRequestEntity.class))).thenAnswer(i -> {
            ReturnRequestEntity r = i.getArgument(0);
            r.setId(50L);
            return r;
        });
        when(orderItemRepository.findById(200L)).thenReturn(Optional.of(orderItem));
        when(returnItemRepository.save(any(ReturnItemEntity.class))).thenAnswer(i -> {
            ReturnItemEntity item = i.getArgument(0);
            item.setId(300L);
            return item;
        });

        ReturnRequestDto dto = ReturnRequestDto.builder()
                .orderId(100L)
                .reason("DAMAGED_IN_TRANSIT")
                .items(List.of(ReturnRequestDto.ReturnItemDto.builder()
                        .orderItemId(200L)
                        .quantity(1)
                        .detailReason("Hàng bị móp méo khi nhận")
                        .build()))
                .build();

        ReturnRequestResponse response = returnService.create(dto);

        assertNotNull(response);
        assertEquals(50L, response.getId());
        assertEquals("DAMAGED_IN_TRANSIT", response.getReason());
        assertEquals(ReturnStatus.REQUESTED, response.getStatus());
        assertEquals(1, response.getItems().size());
        verify(returnRequestRepository).save(any(ReturnRequestEntity.class));
    }

    @Test
    @DisplayName("UC26: Get return requests by order ID returns matching records")
    void testGetByOrder() {
        when(returnRequestRepository.findByOrderId(100L)).thenReturn(List.of(returnRequest));

        List<ReturnRequestResponse> responses = returnService.getByOrder(100L);

        assertEquals(1, responses.size());
        assertEquals(100L, responses.get(0).getOrderId());
        assertEquals("DAMAGED_IN_TRANSIT", responses.get(0).getReason());
    }

    @Test
    @DisplayName("UC26: Update return status updates status and processedBy employee")
    void testUpdateStatus() throws Exception {
        EmployeeEntity employee = EmployeeEntity.builder().id(5L).build();
        when(returnRequestRepository.findById(50L)).thenReturn(Optional.of(returnRequest));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(returnRequestRepository.save(any(ReturnRequestEntity.class))).thenAnswer(i -> i.getArgument(0));

        ReturnRequestResponse response = returnService.updateStatus(50L, ReturnStatus.APPROVED, 5L);

        assertEquals(ReturnStatus.APPROVED, response.getStatus());
        verify(returnRequestRepository).save(returnRequest);
    }
}
