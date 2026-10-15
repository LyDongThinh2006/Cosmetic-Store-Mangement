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
import com.thinh.cosmetic.domain.entity.inventory.InventoryEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.returns.ReturnItemRepository;
import com.thinh.cosmetic.repository.returns.ReturnRequestRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;
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

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ReturnServiceImpl returnService;

    private OrderEntity order;
    private OrderItemEntity orderItem;
    private ReturnRequestEntity returnRequest;
    private StoreEntity store;
    private ProductSkuEntity sku;

    @BeforeEach
    void setUp() {
        CustomerEntity customer = CustomerEntity.builder().id(1L).fullName("Minh Anh").build();
        store = StoreEntity.builder().id(10L).name("LUNEA Quận 1").build();

        order = OrderEntity.builder()
                .id(100L)
                .orderCode("LUN-100")
                .customer(customer)
                .assignedStore(store)
                .status(OrderStatus.COMPLETED)
                .build();

        ProductEntity product = ProductEntity.builder().id(1L).name("Kem Dưỡng").build();
        sku = ProductSkuEntity.builder().id(10L).product(product).variantName("50ml").price(BigDecimal.valueOf(500000)).build();

        orderItem = OrderItemEntity.builder()
                .id(200L)
                .order(order)
                .sku(sku)
                .quantity(2)
                .snapshotPrice(BigDecimal.valueOf(500000))
                .snapshotProductName("Kem Dưỡng")
                .build();

        ReturnItemEntity returnItem = ReturnItemEntity.builder()
                .id(300L)
                .orderItem(orderItem)
                .quantity(1)
                .refundAmount(BigDecimal.valueOf(500000))
                .detailReason("Hàng bị móp méo khi nhận")
                .build();

        List<ReturnItemEntity> items = new ArrayList<>();
        items.add(returnItem);

        returnRequest = ReturnRequestEntity.builder()
                .id(50L)
                .order(order)
                .customer(customer)
                .reason("DAMAGED_IN_TRANSIT")
                .status(ReturnStatus.REQUESTED)
                .refundAmount(BigDecimal.valueOf(500000))
                .requestedAt(LocalDateTime.now())
                .items(items)
                .build();
    }

    @Test
    @DisplayName("UC26: Create return request creates return entity and items successfully")
    void testCreateReturnRequest() throws Exception {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(returnRequestRepository.save(any(ReturnRequestEntity.class))).thenAnswer(i -> {
            ReturnRequestEntity r = i.getArgument(0);
            r.setId(50L);
            return r;
        });
        when(orderItemRepository.findById(200L)).thenReturn(Optional.of(orderItem));
        when(returnItemRepository.findByOrderItemId(200L)).thenReturn(List.of());
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

        ReturnRequestResponse response = returnService.create(1L, dto);

        assertNotNull(response);
        assertEquals(50L, response.getId());
        assertEquals("DAMAGED_IN_TRANSIT", response.getReason());
        assertEquals(ReturnStatus.REQUESTED, response.getStatus());
        assertEquals(1, response.getItems().size());
        assertEquals(1L, response.getCustomerId());
        verify(returnRequestRepository).save(any(ReturnRequestEntity.class));
    }

    @Test
    @DisplayName("UC26: Không cho phép trả hàng nếu đơn chưa COMPLETED")
    void testCreateReturnRequest_OrderNotCompleted() {
        order.setStatus(OrderStatus.SHIPPING);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        ReturnRequestDto dto = ReturnRequestDto.builder()
                .orderId(100L)
                .reason("DAMAGED_IN_TRANSIT")
                .items(List.of(ReturnRequestDto.ReturnItemDto.builder().orderItemId(200L).quantity(1).build()))
                .build();

        assertThrows(BusinessException.class, () -> returnService.create(1L, dto));
    }

    @Test
    @DisplayName("UC26: Không cho phép khách hàng trả đơn của khách khác (ACCESS_DENIED)")
    void testCreateReturnRequest_OtherCustomerOrder() {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));

        ReturnRequestDto dto = ReturnRequestDto.builder()
                .orderId(100L)
                .reason("DAMAGED_IN_TRANSIT")
                .items(List.of(ReturnRequestDto.ReturnItemDto.builder().orderItemId(200L).quantity(1).build()))
                .build();

        // Customer ID 999 is different from order customer ID 1
        assertThrows(BusinessException.class, () -> returnService.create(999L, dto));
    }

    @Test
    @DisplayName("UC26: Giới hạn số lượng trả không được vượt quá số lượng đã mua")
    void testCreateReturnRequest_ExceedsQuantity() {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findById(200L)).thenReturn(Optional.of(orderItem)); // quantity = 2
        when(returnItemRepository.findByOrderItemId(200L)).thenReturn(List.of());

        ReturnRequestDto dto = ReturnRequestDto.builder()
                .orderId(100L)
                .reason("DAMAGED_IN_TRANSIT")
                .items(List.of(ReturnRequestDto.ReturnItemDto.builder()
                        .orderItemId(200L)
                        .quantity(5) // requests 5 > 2
                        .build()))
                .build();

        assertThrows(BusinessException.class, () -> returnService.create(1L, dto));
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
    @DisplayName("UC26: Duyệt đơn đổi trả (REQUESTED -> APPROVED)")
    void testUpdateStatus_Approved() throws Exception {
        EmployeeEntity employee = EmployeeEntity.builder().id(5L).fullName("Trần Quản Lý").build();
        when(returnRequestRepository.findById(50L)).thenReturn(Optional.of(returnRequest));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(returnRequestRepository.save(any(ReturnRequestEntity.class))).thenAnswer(i -> i.getArgument(0));

        ReturnRequestResponse response = returnService.updateStatus(50L, ReturnStatus.APPROVED, 5L);

        assertEquals(ReturnStatus.APPROVED, response.getStatus());
        verify(returnRequestRepository).save(returnRequest);
    }

    @Test
    @DisplayName("UC26: Hoàn tất đổi trả (APPROVED -> COMPLETED) tự động cộng lại tồn kho chi nhánh")
    void testUpdateStatus_Completed_RestocksInventory() throws Exception {
        returnRequest.setStatus(ReturnStatus.APPROVED);
        EmployeeEntity employee = EmployeeEntity.builder().id(5L).fullName("Trần Quản Lý").build();

        InventoryEntity inventory = InventoryEntity.builder()
                .id(1L)
                .store(store)
                .sku(sku)
                .actualStock(10)
                .heldQuantity(0)
                .build();

        when(returnRequestRepository.findById(50L)).thenReturn(Optional.of(returnRequest));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));
        when(inventoryRepository.findByStoreIdAndSkuId(10L, 10L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(returnRequestRepository.save(any(ReturnRequestEntity.class))).thenAnswer(i -> i.getArgument(0));

        ReturnRequestResponse response = returnService.updateStatus(50L, ReturnStatus.COMPLETED, 5L);

        assertEquals(ReturnStatus.COMPLETED, response.getStatus());
        // Verify inventory increased by returned quantity (10 + 1 = 11)
        assertEquals(11, inventory.getActualStock());
        verify(inventoryRepository).save(inventory);
    }

    @Test
    @DisplayName("UC26: Không cho phép chuyển trạng thái không hợp lệ (COMPLETED -> REQUESTED)")
    void testUpdateStatus_InvalidTransition() {
        returnRequest.setStatus(ReturnStatus.COMPLETED);
        EmployeeEntity employee = EmployeeEntity.builder().id(5L).build();
        when(returnRequestRepository.findById(50L)).thenReturn(Optional.of(returnRequest));
        when(employeeRepository.findById(5L)).thenReturn(Optional.of(employee));

        assertThrows(BusinessException.class, () -> returnService.updateStatus(50L, ReturnStatus.REQUESTED, 5L));
    }
}

