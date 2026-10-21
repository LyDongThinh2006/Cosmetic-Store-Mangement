package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import com.thinh.cosmetic.domain.entity.sales.CartItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderReservationEntity;
import com.thinh.cosmetic.domain.entity.inventory.InventoryEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.ReservationStatus;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.PaymentMethod;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.repository.account.CustomerAddressRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.CartItemRepository;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.order.OrderReservationRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.security.StoreScope;
import com.thinh.cosmetic.service.order.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private OrderReservationRepository stockHoldRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerAddressRepository addressRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private ProductImageRepository productImageRepository;
    @Mock private StoreScope storeScope;
    @Mock private com.thinh.cosmetic.service.order.VoucherService voucherService;
    @Mock private com.thinh.cosmetic.repository.order.VoucherRepository voucherRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private CustomerEntity customer;
    private CustomerAddressEntity address;
    private CartEntity cart;
    private ProductSkuEntity sku;
    private StoreEntity store;
    private InventoryEntity inventory;

    @BeforeEach
    void setUp() {
        AccountEntity account = AccountEntity.builder().id(101L).phone("0987654321").email("khach@lunea.test").build();
        customer = CustomerEntity.builder().id(1L).fullName("Nguyen Van A").account(account).build();
        address = CustomerAddressEntity.builder()
                .id(10L)
                .customerEntity(customer)
                .recipientName("Nguyen Van A")
                .phone("0987654321")
                .addressDetail("123 Le Loi")
                .ward("Phuong Ben Nghe")
                .district("Quan 1")
                .province("TP. Ho Chi Minh")
                .build();

        ProductEntity product = ProductEntity.builder().id(1L).name("Son Kem Li Lunea").build();
        sku = ProductSkuEntity.builder()
                .id(100L)
                .skuCode("SKU-001")
                .variantName("Do Cam")
                .price(new BigDecimal("200000"))
                .product(product)
                .build();

        cart = CartEntity.builder().id(5L).customer(customer).build();
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(2)
                .build();

        store = StoreEntity.builder().id(1L).name("Lunea Store 1").province("Ho Chi Minh").build();
        inventory = InventoryEntity.builder()
                .id(200L)
                .store(store)
                .sku(sku)
                .actualStock(50)
                .heldQuantity(0)
                .build();
    }

    @Test
    @DisplayName("Place order calculates total, reserves stock and clears cart")
    void testPlaceOrderSuccess() throws Exception {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(2)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(10L)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));

        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> {
            OrderEntity o = i.getArgument(0);
            o.setId(999L);
            return o;
        });
        when(orderItemRepository.save(any(OrderItemEntity.class))).thenAnswer(i -> i.getArgument(0));

        OrderRequest request = OrderRequest.builder()
                .customerAddressId(10L)
                .paymentMethod(PaymentMethod.COD)
                .note("Giao gio hanh chinh")
                .build();

        OrderResponse response = orderService.placeOrder(1L, request);

        assertNotNull(response);
        assertEquals(new BigDecimal("400000"), response.getSubtotal()); // 2 * 200,000
        assertEquals(new BigDecimal("30000"), response.getShippingFee()); // < 500,000 threshold
        assertEquals(new BigDecimal("430000"), response.getTotalAmount());
        assertEquals(OrderStatus.PENDING, response.getStatus());

        // Verify stock hold was created
        verify(stockHoldRepository).save(argThat(h -> h.getQuantity() == 2 && h.getStatus() == ReservationStatus.HELD));
        // Verify inventory heldQuantity was increased
        assertEquals(2, inventory.getHeldQuantity());
        // Verify cart was cleared
        verify(cartItemRepository).deleteByCartId(5L);
    }

    @Test
    @DisplayName("Place order with voucher LUNEA10 applies 10% discount")
    void testPlaceOrderWithVoucher() throws Exception {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(2)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(10L)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));

        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> {
            OrderEntity o = i.getArgument(0);
            o.setId(999L);
            return o;
        });
        when(orderItemRepository.save(any(OrderItemEntity.class))).thenAnswer(i -> i.getArgument(0));

        com.thinh.cosmetic.domain.entity.sales.VoucherEntity mockVoucher = com.thinh.cosmetic.domain.entity.sales.VoucherEntity.builder()
                .id(1L)
                .code("LUNEA10")
                .discountType(com.thinh.cosmetic.domain.enums.VoucherType.PERCENT)
                .discountValue(new BigDecimal("10.00"))
                .build();
        when(voucherService.validateAndApply(eq("LUNEA10"), any(BigDecimal.class))).thenReturn(mockVoucher);
        when(voucherService.calculateDiscount(eq(mockVoucher), any(BigDecimal.class))).thenReturn(new BigDecimal("40000.00"));
        when(voucherService.calculateShippingDiscount(eq(mockVoucher), any(BigDecimal.class))).thenReturn(BigDecimal.ZERO);

        OrderRequest request = OrderRequest.builder()
                .customerAddressId(10L)
                .paymentMethod(PaymentMethod.COD)
                .voucherCode("LUNEA10")
                .build();

        OrderResponse response = orderService.placeOrder(1L, request);

        assertNotNull(response);
        assertEquals(new BigDecimal("400000"), response.getSubtotal());
        assertEquals(new BigDecimal("40000.00"), response.getDiscountAmount()); // 10% of 400,000
        assertEquals(new BigDecimal("30000"), response.getShippingFee());
        assertEquals(new BigDecimal("390000.00"), response.getTotalAmount()); // 400,000 - 40,000 + 30,000
    }

    @Test
    @DisplayName("Customer can cancel pending order and held stock is released")
    void testCancelPendingOrderSuccess() throws Exception {
        OrderEntity order = OrderEntity.builder()
                .id(999L)
                .orderCode("LUN-123456")
                .customer(customer)
                .status(OrderStatus.PENDING)
                .assignedStore(store)
                .subtotal(new BigDecimal("400000"))
                .discountAmount(BigDecimal.ZERO)
                .shippingFee(new BigDecimal("30000"))
                .totalAmount(new BigDecimal("430000"))
                .build();

        OrderReservationEntity hold = OrderReservationEntity.builder()
                .id(1L)
                .order(order)
                .store(store)
                .sku(sku)
                .quantity(2)
                .status(ReservationStatus.HELD)
                .build();

        inventory.setHeldQuantity(2);

        when(orderRepository.findById(999L)).thenReturn(Optional.of(order));
        when(stockHoldRepository.findByOrderId(999L)).thenReturn(List.of(hold));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse cancelled = orderService.cancelOrder(999L, 1L);

        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
        assertEquals(ReservationStatus.RELEASED, hold.getStatus());
        assertEquals(0, inventory.getHeldQuantity());
    }

    @Test
    @DisplayName("Cancel order fails if order is already CONFIRMED or SHIPPING")
    void testCancelOrderNotPendingFails() {
        OrderEntity order = OrderEntity.builder()
                .id(999L)
                .orderCode("LUN-123456")
                .customer(customer)
                .status(OrderStatus.SHIPPING)
                .build();

        when(orderRepository.findById(999L)).thenReturn(Optional.of(order));

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.cancelOrder(999L, 1L));
        assertEquals(ErrorCode.ORDER_NOT_CANCELLABLE, ex.getErrorCode());
    }

    @Test
    @DisplayName("Shipping status commits hold and deducts actual stock")
    void testUpdateStatusShippingDeductsStock() throws Exception {
        OrderEntity order = OrderEntity.builder()
                .id(999L)
                .orderCode("LUN-123456")
                .customer(customer)
                .status(OrderStatus.PREPARING)
                .assignedStore(store)
                .subtotal(new BigDecimal("400000"))
                .discountAmount(BigDecimal.ZERO)
                .shippingFee(new BigDecimal("30000"))
                .totalAmount(new BigDecimal("430000"))
                .build();

        OrderReservationEntity hold = OrderReservationEntity.builder()
                .id(1L)
                .order(order)
                .store(store)
                .sku(sku)
                .quantity(2)
                .status(ReservationStatus.HELD)
                .build();

        inventory.setActualStock(50);
        inventory.setHeldQuantity(2);

        when(orderRepository.findById(999L)).thenReturn(Optional.of(order));
        when(stockHoldRepository.findByOrderId(999L)).thenReturn(List.of(hold));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse shipping = orderService.updateStatus(999L, OrderStatus.SHIPPING);

        assertEquals(OrderStatus.SHIPPING, shipping.getStatus());
        assertEquals(ReservationStatus.COMMITTED, hold.getStatus());
        assertEquals(48, inventory.getActualStock()); // 50 - 2
        assertEquals(0, inventory.getHeldQuantity()); // 2 - 2
    }

    @Test
    @DisplayName("Staff can only access orders within their store scope")
    void testStoreScopeOrdersFiltering() {
        AuthPrincipal staffHcm = AuthPrincipal.builder()
                .employeeId(2L)
                .username("order_hcm")
                .build();

        when(storeScope.getAccessibleStoreIds(staffHcm)).thenReturn(List.of(1L));
        when(orderRepository.findByAssignedStoreIdInOrderByCreatedAtDesc(List.of(1L))).thenReturn(List.of());

        List<OrderResponse> orders = orderService.getOrders(staffHcm, null);
        assertNotNull(orders);
        verify(orderRepository).findByAssignedStoreIdInOrderByCreatedAtDesc(List.of(1L));
    }

    @Test
    @DisplayName("Place order fails if customer requested store does not have enough stock")
    void testPlaceOrderRequestedStoreInsufficientStockFails() {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(10)
                .build();

        inventory.setActualStock(5);
        inventory.setHeldQuantity(0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));

        OrderRequest request = OrderRequest.builder()
                .fulfillmentStoreId(1L)
                .build();

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.placeOrder(1L, request));
        assertEquals(ErrorCode.INSUFFICIENT_STOCK, ex.getErrorCode());
        verify(orderRepository, never()).save(any());
        verify(stockHoldRepository, never()).save(any());
    }

    @Test
    @DisplayName("Place order fails if no store can fulfill all items (no fallback to default store)")
    void testPlaceOrderNoStoreCanFulfillFails() {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(10)
                .build();

        inventory.setActualStock(5); // Only 5 in stock, needs 10
        inventory.setHeldQuantity(0);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));

        OrderRequest request = OrderRequest.builder().build();

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.placeOrder(1L, request));
        assertEquals(ErrorCode.NO_STORE_CAN_FULFILL, ex.getErrorCode());
        verify(orderRepository, never()).save(any());
        verify(stockHoldRepository, never()).save(any());
    }

    @Test
    @DisplayName("Place order fails if inventory record is missing when holding stock")
    void testPlaceOrderMissingInventoryRecordFails() {
        CartItemEntity cartItem = CartItemEntity.builder()
                .id(50L)
                .cart(cart)
                .sku(sku)
                .quantity(2)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findAll()).thenReturn(List.of(store));
        // First call during capableStores check finds inventory, but during reserve it's missing
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L))
                .thenReturn(Optional.of(inventory))
                .thenReturn(Optional.empty());

        OrderRequest request = OrderRequest.builder().build();

        BusinessException ex = assertThrows(BusinessException.class, () -> orderService.placeOrder(1L, request));
        assertEquals(ErrorCode.INSUFFICIENT_STOCK, ex.getErrorCode());
        // Verify stockHold was never saved because inventory lookup failed first
        verify(stockHoldRepository, never()).save(any());
    }

    @Test
    @DisplayName("Invalid status transitions are rejected")
    void testInvalidStatusTransitions() {
        OrderEntity pendingOrder = OrderEntity.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(pendingOrder));

        // PENDING -> SHIPPING should fail
        BusinessException ex1 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(1L, OrderStatus.SHIPPING));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex1.getErrorCode());

        // PENDING -> COMPLETED should fail
        BusinessException ex2 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(1L, OrderStatus.COMPLETED));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex2.getErrorCode());

        OrderEntity shippingOrder = OrderEntity.builder()
                .id(2L)
                .status(OrderStatus.SHIPPING)
                .build();
        when(orderRepository.findById(2L)).thenReturn(Optional.of(shippingOrder));

        // SHIPPING -> CANCELLED should fail
        BusinessException ex3 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(2L, OrderStatus.CANCELLED));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex3.getErrorCode());

        // SHIPPING -> PREPARING should fail
        BusinessException ex4 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(2L, OrderStatus.PREPARING));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex4.getErrorCode());

        OrderEntity completedOrder = OrderEntity.builder()
                .id(3L)
                .status(OrderStatus.COMPLETED)
                .build();
        when(orderRepository.findById(3L)).thenReturn(Optional.of(completedOrder));

        // COMPLETED -> CANCELLED should fail
        BusinessException ex5 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(3L, OrderStatus.CANCELLED));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex5.getErrorCode());

        OrderEntity cancelledOrder = OrderEntity.builder()
                .id(4L)
                .status(OrderStatus.CANCELLED)
                .build();
        when(orderRepository.findById(4L)).thenReturn(Optional.of(cancelledOrder));

        // CANCELLED -> PENDING should fail
        BusinessException ex6 = assertThrows(BusinessException.class,
                () -> orderService.updateStatus(4L, OrderStatus.PENDING));
        assertEquals(ErrorCode.INVALID_STATUS_TRANSITION, ex6.getErrorCode());
    }

    @Test
    @DisplayName("Full valid status workflow: PENDING -> CONFIRMED -> PREPARING -> SHIPPING -> COMPLETED")
    void testValidStatusTransitionsFullWorkflow() throws Exception {
        OrderEntity order = OrderEntity.builder()
                .id(100L)
                .status(OrderStatus.PENDING)
                .assignedStore(store)
                .build();
        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(i -> i.getArgument(0));

        // 1. PENDING -> CONFIRMED
        OrderResponse res1 = orderService.updateStatus(100L, OrderStatus.CONFIRMED);
        assertEquals(OrderStatus.CONFIRMED, res1.getStatus());
        assertNotNull(order.getConfirmedAt());

        // 2. CONFIRMED -> PREPARING
        OrderResponse res2 = orderService.updateStatus(100L, OrderStatus.PREPARING);
        assertEquals(OrderStatus.PREPARING, res2.getStatus());

        // 3. PREPARING -> SHIPPING
        OrderReservationEntity hold = OrderReservationEntity.builder()
                .id(1L)
                .order(order)
                .store(store)
                .sku(sku)
                .quantity(2)
                .status(ReservationStatus.HELD)
                .build();
        inventory.setActualStock(10);
        inventory.setHeldQuantity(2);
        when(stockHoldRepository.findByOrderId(100L)).thenReturn(List.of(hold));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));

        OrderResponse res3 = orderService.updateStatus(100L, OrderStatus.SHIPPING);
        assertEquals(OrderStatus.SHIPPING, res3.getStatus());
        assertEquals(ReservationStatus.COMMITTED, hold.getStatus());
        assertEquals(8, inventory.getActualStock());
        assertEquals(0, inventory.getHeldQuantity());

        // 4. SHIPPING -> COMPLETED
        OrderResponse res4 = orderService.updateStatus(100L, OrderStatus.COMPLETED);
        assertEquals(OrderStatus.COMPLETED, res4.getStatus());
        assertNotNull(order.getCompletedAt());
    }
}
