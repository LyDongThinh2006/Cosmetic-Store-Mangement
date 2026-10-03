package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import com.thinh.cosmetic.domain.entity.sales.CartItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.entity.inventory.InventoryEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.ReservationStatus;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.PaymentMethod;
import com.thinh.cosmetic.repository.account.CustomerAddressRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.CartItemRepository;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.order.OrderReservationRepository;

import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
        customer = CustomerEntity.builder().id(1L).fullName("Nguyen Van A").build();
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

        store = StoreEntity.builder().id(1L).name("Lunea Store 1").build();
        inventory = InventoryEntity.builder()
                .id(200L)
                .store(store)
                .sku(sku)
                .actualStock(50)
                .heldQuantity(0)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(10L)).thenReturn(Optional.of(address));
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(5L)).thenReturn(List.of(cartItem));
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 100L)).thenReturn(Optional.of(inventory));
    }

    @Test
    @DisplayName("Place order calculates total, reserves stock and clears cart")
    void testPlaceOrderSuccess() throws Exception {
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
}
