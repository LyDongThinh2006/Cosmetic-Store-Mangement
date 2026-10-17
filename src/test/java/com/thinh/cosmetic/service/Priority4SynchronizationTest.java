package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.request.cart.CartItemRequest;
import com.thinh.cosmetic.domain.entity.account.AccountEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import com.thinh.cosmetic.domain.entity.sales.CartItemEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.entity.catalog.BrandEntity;
import com.thinh.cosmetic.domain.entity.catalog.CategoryEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.mapper.catalog.BrandMapper;
import com.thinh.cosmetic.mapper.catalog.CategoryMapper;
import com.thinh.cosmetic.mapper.catalog.ProductMapper;
import com.thinh.cosmetic.mapper.store.StoreMapper;
import com.thinh.cosmetic.repository.account.*;
import com.thinh.cosmetic.repository.cart.CartItemRepository;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.catalog.*;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.purchase.PurchaseOrderItemRepository;
import com.thinh.cosmetic.repository.review.ReviewRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.repository.store.StockTransferItemRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.security.AppUserDetailsService;
import com.thinh.cosmetic.service.account.impl.CustomerServiceImpl;
import com.thinh.cosmetic.service.account.impl.EmployeeServiceImpl;
import com.thinh.cosmetic.service.cart.impl.CartServiceImpl;
import com.thinh.cosmetic.service.catalog.impl.BrandServiceImpl;
import com.thinh.cosmetic.service.catalog.impl.CategoryServiceImpl;
import com.thinh.cosmetic.service.catalog.impl.ProductServiceImpl;
import com.thinh.cosmetic.service.store.impl.StoreServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Ưu tiên 4: Đồng bộ trạng thái và dữ liệu quản trị Tests")
class Priority4SynchronizationTest {

    @Nested
    @DisplayName("1. Đồng bộ trạng thái Nhân viên & Tài khoản")
    class EmployeeStatusSyncTests {

        @Mock
        private EmployeeRepository employeeRepository;
        @Mock
        private AccountRepository accountRepository;
        @Mock
        private RoleRepository roleRepository;
        @Mock
        private StoreRepository storeRepository;
        @Mock
        private EmployeeRoleRepository employeeRoleRepository;
        @Mock
        private EmployeeStoreRepository employeeStoreRepository;
        @Mock
        private PasswordEncoder passwordEncoder;

        @InjectMocks
        private EmployeeServiceImpl employeeService;

        @Test
        @DisplayName("Deactivate nhân viên phải đồng bộ khóa/ngừng tài khoản đăng nhập")
        void deactivateEmployee_shouldSyncAccountStatusToInactive() throws Exception {
            AccountEntity account = AccountEntity.builder()
                    .id(10L)
                    .username("nv_test@lunea.test")
                    .status(ActiveStatus.ACTIVE)
                    .accountType(AccountType.EMPLOYEE)
                    .build();

            EmployeeEntity employee = EmployeeEntity.builder()
                    .id(1L)
                    .fullName("Nguyễn Văn A")
                    .status(ActiveStatus.ACTIVE)
                    .account(account)
                    .build();

            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            employeeService.deactivate(1L);

            assertEquals(ActiveStatus.INACTIVE, employee.getStatus());
            assertEquals(ActiveStatus.INACTIVE, account.getStatus());
        }

        @Test
        @DisplayName("Activate nhân viên phải đồng bộ mở khóa tài khoản đăng nhập")
        void activateEmployee_shouldSyncAccountStatusToActive() throws Exception {
            AccountEntity account = AccountEntity.builder()
                    .id(10L)
                    .username("nv_test@lunea.test")
                    .status(ActiveStatus.INACTIVE)
                    .accountType(AccountType.EMPLOYEE)
                    .build();

            EmployeeEntity employee = EmployeeEntity.builder()
                    .id(1L)
                    .fullName("Nguyễn Văn A")
                    .status(ActiveStatus.INACTIVE)
                    .account(account)
                    .build();

            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

            employeeService.activate(1L);

            assertEquals(ActiveStatus.ACTIVE, employee.getStatus());
            assertEquals(ActiveStatus.ACTIVE, account.getStatus());
        }

        @Test
        @DisplayName("AppUserDetailsService phải từ chối xác thực nếu nhân viên bị ngừng hoạt động")
        void userDetailsService_shouldRejectInactiveEmployee() {
            AccountRepository accRepo = mock(AccountRepository.class);
            CustomerRepository custRepo = mock(CustomerRepository.class);
            EmployeeRepository empRepo = mock(EmployeeRepository.class);
            EmployeeRoleRepository empRoleRepo = mock(EmployeeRoleRepository.class);
            RolePermissionRepository rolePermRepo = mock(RolePermissionRepository.class);

            AppUserDetailsService userDetailsService = new AppUserDetailsService(
                    accRepo,
                    custRepo,
                    empRepo,
                    empRoleRepo,
                    rolePermRepo
            );

            AccountEntity account = AccountEntity.builder()
                    .id(10L)
                    .username("nv_inactive@lunea.test")
                    .passwordHash("hashed")
                    .status(ActiveStatus.ACTIVE)
                    .accountType(AccountType.EMPLOYEE)
                    .build();

            EmployeeEntity employee = EmployeeEntity.builder()
                    .id(1L)
                    .account(account)
                    .status(ActiveStatus.INACTIVE)
                    .build();

            when(accRepo.findByEmailIgnoreCaseOrPhoneOrUsernameIgnoreCase(any(), any(), any()))
                    .thenReturn(Optional.of(account));
            when(empRepo.findByAccountId(10L)).thenReturn(Optional.of(employee));

            BadCredentialsException ex = assertThrows(BadCredentialsException.class, () ->
                    userDetailsService.loadUserByUsername("nv_inactive@lunea.test")
            );

            assertTrue(ex.getMessage().contains("tài khoản đã bị khóa"));
        }
    }

    @Nested
    @DisplayName("2. Ngừng hoạt động Cửa hàng & Luồng chọn chi nhánh")
    class StoreStatusSyncTests {

        @Mock
        private StoreRepository storeRepository;
        @Mock
        private StoreMapper storeMapper;

        @InjectMocks
        private StoreServiceImpl storeService;

        @Test
        @DisplayName("getActiveStores chỉ lấy các cửa hàng có trạng thái ACTIVE")
        void getActiveStores_shouldOnlyQueryActiveStores() {
            StoreEntity activeStore = StoreEntity.builder()
                    .id(1L)
                    .name("Chi nhánh Quận 1")
                    .status(ActiveStatus.ACTIVE)
                    .build();

            when(storeRepository.findByStatus(ActiveStatus.ACTIVE)).thenReturn(List.of(activeStore));

            storeService.getActiveStores();

            verify(storeRepository, times(1)).findByStatus(ActiveStatus.ACTIVE);
            verify(storeRepository, never()).findAll();
        }

        @Test
        @DisplayName("Deactivate cửa hàng chuyển trạng thái thành INACTIVE")
        void deactivateStore_shouldUpdateStatusToInactive() throws Exception {
            StoreEntity store = StoreEntity.builder()
                    .id(1L)
                    .name("Chi nhánh Đóng Cửa")
                    .status(ActiveStatus.ACTIVE)
                    .build();

            when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

            storeService.deactivate(1L);

            assertEquals(ActiveStatus.INACTIVE, store.getStatus());
        }
    }

    @Nested
    @DisplayName("3. Xác nhận quyền sở hữu địa chỉ và giỏ hàng")
    class OwnershipVerificationTests {

        @Mock
        private CustomerRepository customerRepository;
        @Mock
        private CustomerAddressRepository customerAddressRepository;

        @InjectMocks
        private CustomerServiceImpl customerService;

        @Test
        @DisplayName("Sửa địa chỉ của khách hàng khác phải bị từ chối với ACCESS_DENIED")
        void updateAddress_foreignCustomer_shouldThrowAccessDenied() {
            CustomerEntity owner = CustomerEntity.builder().id(100L).build();
            CustomerAddressEntity address = CustomerAddressEntity.builder()
                    .id(5L)
                    .customerEntity(owner)
                    .build();

            when(customerAddressRepository.findById(5L)).thenReturn(Optional.of(address));

            CustomerAddressRequest request = CustomerAddressRequest.builder()
                    .recipientName("Hacker")
                    .phone("0909090909")
                    .province("HCM")
                    .district("Q1")
                    .ward("Ben Nghe")
                    .addressDetail("123 Le Loi")
                    .build();

            BusinessException ex = assertThrows(BusinessException.class, () ->
                    customerService.updateAddress(999L, 5L, request)
            );

            assertEquals(ErrorCode.ACCESS_DENIED, ex.getErrorCode());
        }

        @Test
        @DisplayName("Xóa địa chỉ của khách hàng khác phải bị từ chối với ACCESS_DENIED")
        void deleteAddress_foreignCustomer_shouldThrowAccessDenied() {
            CustomerEntity owner = CustomerEntity.builder().id(100L).build();
            CustomerAddressEntity address = CustomerAddressEntity.builder()
                    .id(5L)
                    .customerEntity(owner)
                    .build();

            when(customerAddressRepository.findById(5L)).thenReturn(Optional.of(address));

            BusinessException ex = assertThrows(BusinessException.class, () ->
                    customerService.deleteAddress(999L, 5L)
            );

            assertEquals(ErrorCode.ACCESS_DENIED, ex.getErrorCode());
        }

        @Test
        @DisplayName("Cập nhật số lượng mặt hàng không thuộc giỏ hàng của mình phải bị từ chối")
        void updateCartItem_foreignCart_shouldThrowAccessDenied() {
            CartRepository cartRepository = mock(CartRepository.class);
            CartItemRepository cartItemRepository = mock(CartItemRepository.class);
            CustomerRepository custRepo = mock(CustomerRepository.class);
            ProductSkuRepository productSkuRepository = mock(ProductSkuRepository.class);
            ProductImageRepository productImageRepository = mock(ProductImageRepository.class);
            InventoryRepository inventoryRepository = mock(InventoryRepository.class);

            CartServiceImpl cartService = new CartServiceImpl(
                    cartRepository,
                    cartItemRepository,
                    custRepo,
                    productSkuRepository,
                    productImageRepository,
                    inventoryRepository
            );

            CustomerEntity customer = CustomerEntity.builder().id(100L).build();
            CartEntity myCart = CartEntity.builder().id(10L).customer(customer).build();
            CartEntity foreignCart = CartEntity.builder().id(20L).build();

            CartItemEntity foreignItem = CartItemEntity.builder()
                    .id(77L)
                    .cart(foreignCart)
                    .quantity(1)
                    .build();

            when(cartRepository.findByCustomerId(100L)).thenReturn(Optional.of(myCart));
            when(cartItemRepository.findById(77L)).thenReturn(Optional.of(foreignItem));

            BusinessException ex = assertThrows(BusinessException.class, () ->
                    cartService.updateItemQuantity(100L, 77L, 5)
            );

            assertEquals(ErrorCode.ACCESS_DENIED, ex.getErrorCode());
        }

        @Test
        @DisplayName("Thêm sản phẩm ngừng kinh doanh vào giỏ hàng phải bị từ chối")
        void addCartItem_inactiveProduct_shouldThrowProductNotAvailable() {
            CartRepository cartRepository = mock(CartRepository.class);
            CartItemRepository cartItemRepository = mock(CartItemRepository.class);
            CustomerRepository custRepo = mock(CustomerRepository.class);
            ProductSkuRepository productSkuRepository = mock(ProductSkuRepository.class);
            ProductImageRepository productImageRepository = mock(ProductImageRepository.class);
            InventoryRepository inventoryRepository = mock(InventoryRepository.class);

            CartServiceImpl cartService = new CartServiceImpl(
                    cartRepository,
                    cartItemRepository,
                    custRepo,
                    productSkuRepository,
                    productImageRepository,
                    inventoryRepository
            );

            CustomerEntity customer = CustomerEntity.builder().id(100L).build();
            CartEntity myCart = CartEntity.builder().id(10L).customer(customer).build();
            ProductEntity inactiveProduct = ProductEntity.builder()
                    .id(50L)
                    .name("Son đã ngừng bán")
                    .status(ActiveStatus.INACTIVE)
                    .build();

            ProductSkuEntity sku = ProductSkuEntity.builder()
                    .id(501L)
                    .skuCode("SKU-INACTIVE")
                    .product(inactiveProduct)
                    .status(ActiveStatus.ACTIVE)
                    .price(BigDecimal.valueOf(100000))
                    .build();

            when(cartRepository.findByCustomerId(100L)).thenReturn(Optional.of(myCart));
            when(productSkuRepository.findById(501L)).thenReturn(Optional.of(sku));

            CartItemRequest req = CartItemRequest.builder()
                    .skuId(501L)
                    .quantity(1)
                    .build();

            BusinessException ex = assertThrows(BusinessException.class, () ->
                    cartService.addItem(100L, req)
            );

            assertEquals(ErrorCode.PRODUCT_NOT_AVAILABLE, ex.getErrorCode());
        }
    }

    @Nested
    @DisplayName("4. Chính sách xóa an toàn Thương hiệu, Danh mục và Sản phẩm")
    class SafeDeletionPolicyTests {

        @Nested
        @DisplayName("Chính sách xóa Thương hiệu")
        class BrandSafeDeleteTests {
            @Mock
            private BrandRepository brandRepository;
            @Mock
            private ProductRepository productRepository;
            @Mock
            private BrandMapper brandMapper;

            @InjectMocks
            private BrandServiceImpl brandService;

            @Test
            @DisplayName("Thương hiệu đã có sản phẩm tham chiếu -> chuyển sang INACTIVE (Soft Delete)")
            void deleteBrand_withReferencedProducts_shouldSoftDelete() throws Exception {
                BrandEntity brand = BrandEntity.builder()
                        .id(1L)
                        .name("L'Oreal")
                        .status(ActiveStatus.ACTIVE)
                        .build();

                when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
                when(productRepository.countByBrandId(1L)).thenReturn(5L);

                brandService.delete(1L);

                assertEquals(ActiveStatus.INACTIVE, brand.getStatus());
                verify(brandRepository, never()).delete(any(BrandEntity.class));
            }

            @Test
            @DisplayName("Thương hiệu chưa có sản phẩm tham chiếu -> xóa vĩnh viễn (Hard Delete)")
            void deleteBrand_withoutProducts_shouldHardDelete() throws Exception {
                BrandEntity brand = BrandEntity.builder()
                        .id(1L)
                        .name("Unused Brand")
                        .status(ActiveStatus.ACTIVE)
                        .build();

                when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
                when(productRepository.countByBrandId(1L)).thenReturn(0L);

                brandService.delete(1L);

                verify(brandRepository, times(1)).delete(brand);
            }
        }

        @Nested
        @DisplayName("Chính sách xóa Danh mục")
        class CategorySafeDeleteTests {
            @Mock
            private CategoryRepository categoryRepository;
            @Mock
            private ProductRepository productRepository;
            @Mock
            private CategoryMapper categoryMapper;

            @InjectMocks
            private CategoryServiceImpl categoryService;

            @Test
            @DisplayName("Danh mục có sản phẩm hoặc danh mục con -> chuyển sang INACTIVE (Soft Delete)")
            void deleteCategory_withDependencies_shouldSoftDelete() throws Exception {
                CategoryEntity cat = CategoryEntity.builder()
                        .id(2L)
                        .name("Chăm sóc da")
                        .status(ActiveStatus.ACTIVE)
                        .build();

                when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat));
                when(categoryRepository.existsByParentId(2L)).thenReturn(true);

                categoryService.delete(2L);

                assertEquals(ActiveStatus.INACTIVE, cat.getStatus());
                verify(categoryRepository, never()).delete(any(CategoryEntity.class));
            }
        }

        @Nested
        @DisplayName("Chính sách xóa Sản phẩm")
        class ProductSafeDeleteTests {
            @Mock
            private ProductRepository productRepository;
            @Mock
            private ProductSkuRepository productSkuRepository;
            @Mock
            private ProductImageRepository productImageRepository;
            @Mock
            private ProductAttributeValueRepository productAttributeValueRepository;
            @Mock
            private CartItemRepository cartItemRepository;
            @Mock
            private OrderItemRepository orderItemRepository;
            @Mock
            private PurchaseOrderItemRepository purchaseOrderItemRepository;
            @Mock
            private StockTransferItemRepository stockTransferItemRepository;
            @Mock
            private InventoryRepository inventoryRepository;
            @Mock
            private ReviewRepository reviewRepository;
            @Mock
            private CategoryRepository categoryRepository;
            @Mock
            private BrandRepository brandRepository;
            @Mock
            private ProductMapper productMapper;

            @InjectMocks
            private ProductServiceImpl productService;

            @Test
            @DisplayName("Sản phẩm đã phát sinh đơn hàng -> chuyển sang INACTIVE bảo toàn lịch sử")
            void deleteProduct_withOrders_shouldSoftDelete() throws Exception {
                ProductEntity product = ProductEntity.builder()
                        .id(10L)
                        .name("Kem chống nắng Anessa")
                        .status(ActiveStatus.ACTIVE)
                        .build();

                ProductSkuEntity sku = ProductSkuEntity.builder()
                        .id(101L)
                        .skuCode("ANE-60ML")
                        .product(product)
                        .status(ActiveStatus.ACTIVE)
                        .build();

                when(productRepository.findById(10L)).thenReturn(Optional.of(product));
                when(productSkuRepository.findByProductId(10L)).thenReturn(List.of(sku));
                when(orderItemRepository.existsBySkuIdIn(List.of(101L))).thenReturn(true);

                productService.delete(10L);

                assertEquals(ActiveStatus.INACTIVE, product.getStatus());
                assertEquals(ActiveStatus.INACTIVE, sku.getStatus());
                verify(productRepository, never()).delete(any(ProductEntity.class));
            }
        }
    }
}
