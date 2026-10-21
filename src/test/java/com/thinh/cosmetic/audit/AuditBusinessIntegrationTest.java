package com.thinh.cosmetic.audit;

import com.thinh.cosmetic.domain.dto.request.account.SendOtpRequest;
import com.thinh.cosmetic.domain.dto.request.account.VerifyOtpRequest;
import com.thinh.cosmetic.domain.dto.request.order.OrderRequest;
import com.thinh.cosmetic.domain.dto.request.purchase.PurchaseOrderRequest;
import com.thinh.cosmetic.domain.dto.request.store.StockTransferRequest;
import com.thinh.cosmetic.domain.dto.response.order.OrderResponse;
import com.thinh.cosmetic.domain.entity.account.*;
import com.thinh.cosmetic.domain.entity.catalog.*;
import com.thinh.cosmetic.domain.entity.inventory.*;
import com.thinh.cosmetic.domain.entity.sales.*;
import com.thinh.cosmetic.domain.enums.*;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.repository.account.*;
import com.thinh.cosmetic.repository.cart.*;
import com.thinh.cosmetic.repository.catalog.*;
import com.thinh.cosmetic.repository.order.*;
import com.thinh.cosmetic.repository.purchase.*;
import com.thinh.cosmetic.repository.store.*;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.account.OtpService;
import com.thinh.cosmetic.service.notification.EmailService;
import com.thinh.cosmetic.service.order.OrderService;
import com.thinh.cosmetic.service.purchase.PurchaseOrderService;
import com.thinh.cosmetic.service.store.StockTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Evidence-oriented audit regressions. Expected failures expose production defects;
 * no production logic is replaced. H2 fixtures are separate from local PostgreSQL.
 * Tests deliberately have no enclosing test transaction: rollback is observed by
 * reading committed database state after the real service transaction completes.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:lunea_qa_audit;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa", "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "logging.level.org.hibernate.SQL=WARN",
        "logging.level.org.springframework.security=WARN",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
@Import(AuditBusinessIntegrationTest.NoOutboundEmail.class)
class AuditBusinessIntegrationTest {
    @DynamicPropertySource
    static void isolatedDatabase(DynamicPropertyRegistry registry) {
        String url=System.getProperty("qa.db.url","jdbc:h2:mem:lunea_qa_audit;DB_CLOSE_DELAY=-1");
        boolean postgres=url.startsWith("jdbc:postgresql:");
        registry.add("spring.datasource.url",() -> url);
        registry.add("spring.datasource.username",() -> System.getProperty("qa.db.user","sa"));
        registry.add("spring.datasource.password",() -> System.getProperty("qa.db.password",""));
        registry.add("spring.datasource.driver-class-name",() -> postgres?"org.postgresql.Driver":"org.h2.Driver");
        registry.add("spring.jpa.properties.hibernate.dialect",() -> postgres?"org.hibernate.dialect.PostgreSQLDialect":"org.hibernate.dialect.H2Dialect");
        registry.add("spring.jpa.database-platform",() -> postgres?"org.hibernate.dialect.PostgreSQLDialect":"org.hibernate.dialect.H2Dialect");
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class NoOutboundEmail {
        @Bean @Primary
        EmailService auditNoOutboundEmailService() {
            return new EmailService() {
                public void sendOtpEmail(String email, String code, int minutes) { }
                public void sendRegistrationOtpEmail(String email, String code, int minutes) { }
            };
        }
    }

    @Autowired MockMvc mvc;
    @Autowired PlatformTransactionManager transactions;
    @Autowired PasswordEncoder encoder;
    @Autowired AccountRepository accounts;
    @Autowired CustomerRepository customers;
    @Autowired CustomerAddressRepository addresses;
    @Autowired EmployeeRepository employees;
    @Autowired EmployeeStoreRepository employeeStores;
    @Autowired BrandRepository brands;
    @Autowired CategoryRepository categories;
    @Autowired ProductRepository products;
    @Autowired ProductSkuRepository skus;
    @Autowired StoreRepository stores;
    @Autowired InventoryRepository inventories;
    @Autowired CartRepository carts;
    @Autowired CartItemRepository cartItems;
    @Autowired OrderRepository orders;
    @Autowired OrderReservationRepository reservations;
    @Autowired PasswordResetOtpRepository resetOtps;
    @Autowired RegistrationOtpRepository registrationOtps;
    @Autowired SupplierRepository suppliers;
    @Autowired PurchaseOrderRepository purchases;
    @Autowired PurchaseOrderItemRepository purchaseItems;
    @Autowired StockTransferRepository transfers;
    @Autowired OrderService orderService;
    @Autowired OtpService otpService;
    @Autowired PurchaseOrderService purchaseService;
    @Autowired StockTransferService transferService;

    private static final AtomicLong SEQUENCE = new AtomicLong();
    private static final String TEST_PASSWORD = "QaOnly123!";
    private Fixture f;

    record Fixture(String tag, AccountEntity accountA, CustomerEntity a, CustomerEntity b,
                   EmployeeEntity employee, StoreEntity hcm, StoreEntity hn,
                   ProductSkuEntity sku, ProductSkuEntity sku2, CartEntity cartA, CartEntity cartB) { }

    @BeforeEach
    void fixture() {
        String tag = "qa-audit-" + SEQUENCE.incrementAndGet();
        String passwordHash = encoder.encode(TEST_PASSWORD);
        f = new TransactionTemplate(transactions).execute(tx -> {
            AccountEntity aa = accounts.save(AccountEntity.builder().username(tag+"-a")
                    .email(tag+"-a@example.test").passwordHash(passwordHash)
                    .accountType(AccountType.CUSTOMER).status(ActiveStatus.ACTIVE).build());
            AccountEntity ab = accounts.save(AccountEntity.builder().username(tag+"-b")
                    .email(tag+"-b@example.test").passwordHash(passwordHash)
                    .accountType(AccountType.CUSTOMER).status(ActiveStatus.ACTIVE).build());
            CustomerEntity a = customers.save(CustomerEntity.builder().account(aa)
                    .fullName("QA Customer A").loyaltyPoints(0).build());
            CustomerEntity b = customers.save(CustomerEntity.builder().account(ab)
                    .fullName("QA Customer B").loyaltyPoints(0).build());
            AccountEntity ae = accounts.save(AccountEntity.builder().username(tag+"-employee")
                    .email(tag+"-employee@example.test").passwordHash(passwordHash)
                    .accountType(AccountType.EMPLOYEE).status(ActiveStatus.ACTIVE).build());
            EmployeeEntity employee = employees.save(EmployeeEntity.builder().account(ae)
                    .fullName("QA HCM Employee").status(ActiveStatus.ACTIVE).build());
            StoreEntity hcm = stores.save(StoreEntity.builder().name(tag+" HCM")
                    .address("QA test address").province("TP. Hồ Chí Minh").status(ActiveStatus.ACTIVE).build());
            StoreEntity hn = stores.save(StoreEntity.builder().name(tag+" HN")
                    .address("QA test address").province("Hà Nội").status(ActiveStatus.ACTIVE).build());
            employeeStores.save(EmployeeStoreEntity.builder().employee(employee).store(hcm).isPrimary(true).build());
            BrandEntity brand = brands.save(BrandEntity.builder().name(tag).status(ActiveStatus.ACTIVE).build());
            CategoryEntity category = categories.save(CategoryEntity.builder().name(tag).status(ActiveStatus.ACTIVE).build());
            ProductEntity product = products.save(ProductEntity.builder().name(tag+" product")
                    .brand(brand).category(category).status(ActiveStatus.ACTIVE).isFeatured(false).build());
            ProductSkuEntity sku = skus.save(ProductSkuEntity.builder().product(product).skuCode(tag+"-SKU1")
                    .variantName("QA variant 1").price(new BigDecimal("265000")).status(ActiveStatus.ACTIVE).build());
            ProductSkuEntity sku2 = skus.save(ProductSkuEntity.builder().product(product).skuCode(tag+"-SKU2")
                    .variantName("QA variant 2").price(new BigDecimal("200000")).status(ActiveStatus.ACTIVE).build());
            inventories.save(InventoryEntity.builder().store(hcm).sku(sku).actualStock(10).heldQuantity(2).build());
            inventories.save(InventoryEntity.builder().store(hn).sku(sku).actualStock(10).heldQuantity(2).build());
            inventories.save(InventoryEntity.builder().store(hcm).sku(sku2).actualStock(1).heldQuantity(0).build());
            CartEntity ca = carts.save(CartEntity.builder().customer(a).build());
            CartEntity cb = carts.save(CartEntity.builder().customer(b).build());
            cartItems.save(CartItemEntity.builder().cart(ca).sku(sku).quantity(3).build());
            cartItems.save(CartItemEntity.builder().cart(cb).sku(sku).quantity(1).build());
            return new Fixture(tag, aa, a, b, employee, hcm, hn, sku, sku2, ca, cb);
        });
        System.out.printf("QA_FIXTURE tag=%s customerA=%d customerB=%d storeHCM=%d storeHN=%d sku=%d%n",
                f.tag(),f.a().getId(),f.b().getId(),f.hcm().getId(),f.hn().getId(),f.sku().getId());
    }

    private OrderRequest request(Long store) {
        return OrderRequest.builder().fulfillmentStoreId(store).recipientName("QA Receiver")
                .recipientPhone("0901234567").shippingAddress("QA test shipping address")
                .ward("QA Ward").district("QA District").province("TP. Hồ Chí Minh")
                .paymentMethod(PaymentMethod.COD).build();
    }
    private InventoryEntity stock(Long store, Long sku) {
        return inventories.findByStoreIdAndSkuId(store,sku).orElseThrow();
    }
    private void stock(Long store, Long sku, int actual, int held) {
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            InventoryEntity i=stock(store,sku);i.setActualStock(actual);i.setHeldQuantity(held);inventories.save(i);
        });
    }
    private RequestPostProcessor employee(String... extra) {
        var auths = java.util.stream.Stream.concat(java.util.stream.Stream.of("ROLE_EMPLOYEE"),
                java.util.Arrays.stream(extra)).map(SimpleGrantedAuthority::new).toList();
        var principal=AuthPrincipal.builder().accountId(f.employee().getAccount().getId())
                .employeeId(f.employee().getId()).accountType(AccountType.EMPLOYEE)
                .username(f.tag()+"-employee").displayName("QA employee").authorities(auths).build();
        return authentication(new UsernamePasswordAuthenticationToken(principal,"unused",auths));
    }
    private RequestPostProcessor customerA() {
        var auths=List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        var principal=AuthPrincipal.builder().accountId(f.accountA().getId()).customerId(f.a().getId())
                .accountType(AccountType.CUSTOMER).displayName("QA A").authorities(auths).build();
        return authentication(new UsernamePasswordAuthenticationToken(principal,"unused",auths));
    }

    @Test @DisplayName("QA-INT-01: Checkout commits snapshot, HELD reservation and clears cart")
    void checkoutPersistsSnapshotAndHold() throws Exception {
        OrderResponse o=orderService.placeOrder(f.a().getId(),request(f.hcm().getId()));
        InventoryEntity i=stock(f.hcm().getId(),f.sku().getId());
        assertAll(() -> assertEquals(OrderStatus.PENDING,o.getStatus()),
                () -> assertEquals(10,i.getActualStock()), () -> assertEquals(5,i.getHeldQuantity()),
                () -> assertTrue(cartItems.findByCartId(f.cartA().getId()).isEmpty()),
                () -> assertEquals(new BigDecimal("795000.00"),orders.findById(o.getId()).orElseThrow().getSubtotal()),
                () -> assertEquals(ReservationStatus.HELD,reservations.findByOrderId(o.getId()).getFirst().getStatus()));
    }

    @Test @DisplayName("QA-INT-02: Failed checkout rolls back the address created before stock selection")
    void failedCheckoutRollsBackAddress() {
        long before=addresses.findByCustomerEntityId(f.a().getId()).size();
        stock(f.hcm().getId(),f.sku().getId(),2,2);
        assertThrows(BusinessException.class,() -> orderService.placeOrder(f.a().getId(),request(f.hcm().getId())));
        assertAll(() -> assertEquals(before,addresses.findByCustomerEntityId(f.a().getId()).size()),
                () -> assertTrue(orders.findByCustomerIdOrderByCreatedAtDesc(f.a().getId()).isEmpty()),
                () -> assertEquals(2,stock(f.hcm().getId(),f.sku().getId()).getHeldQuantity()),
                () -> assertEquals(1,cartItems.findByCartId(f.cartA().getId()).size()));
    }

    @Test @DisplayName("QA-INT-03: Cancel releases the reservation once and preserves actual stock")
    void cancelReleasesHoldExactlyOnce() throws Exception {
        OrderResponse o=orderService.placeOrder(f.a().getId(),request(f.hcm().getId()));
        orderService.cancelOrder(o.getId(),f.a().getId());
        assertThrows(BusinessException.class,() -> orderService.cancelOrder(o.getId(),f.a().getId()));
        assertAll(() -> assertEquals(10,stock(f.hcm().getId(),f.sku().getId()).getActualStock()),
                () -> assertEquals(2,stock(f.hcm().getId(),f.sku().getId()).getHeldQuantity()),
                () -> assertEquals(ReservationStatus.RELEASED,reservations.findByOrderId(o.getId()).getFirst().getStatus()));
    }

    @Test @DisplayName("QA-INT-04: SHIPPING commits stock only once, including a repeated status request")
    void shippingCommitsStockOnlyOnce() throws Exception {
        OrderResponse o=orderService.placeOrder(f.a().getId(),request(f.hcm().getId()));
        for (OrderStatus s:List.of(OrderStatus.CONFIRMED,OrderStatus.PREPARING,OrderStatus.SHIPPING,
                OrderStatus.SHIPPING,OrderStatus.COMPLETED))orderService.updateStatus(o.getId(),s);
        assertAll(() -> assertEquals(7,stock(f.hcm().getId(),f.sku().getId()).getActualStock()),
                () -> assertEquals(2,stock(f.hcm().getId(),f.sku().getId()).getHeldQuantity()),
                () -> assertEquals(ReservationStatus.COMMITTED,reservations.findByOrderId(o.getId()).getFirst().getStatus()));
    }

    @Test @DisplayName("QA-INT-05: Checkout must revalidate a SKU deactivated after it entered the cart")
    void checkoutMustRejectInactiveSku() {
        ProductSkuEntity sku=skus.findById(f.sku().getId()).orElseThrow();
        sku.setStatus(ActiveStatus.INACTIVE);skus.save(sku);
        assertThrows(BusinessException.class,() -> orderService.placeOrder(f.a().getId(),request(f.hcm().getId())),
                "An inactive SKU must not produce an order or inventory hold");
    }

    @Test @DisplayName("QA-INT-06: A missing second purchase SKU must roll back header and first item")
    void purchaseMustRollbackWhenLaterSkuMissing() {
        SupplierEntity supplier=suppliers.save(SupplierEntity.builder().name(f.tag()+" supplier").status(ActiveStatus.ACTIVE).build());
        var req=PurchaseOrderRequest.builder().supplierId(supplier.getId()).receivingStoreId(f.hcm().getId())
                .items(List.of(PurchaseOrderRequest.PurchaseItemRequest.builder().skuId(f.sku().getId())
                                .quantity(1).unitPrice(new BigDecimal("100000")).build(),
                        PurchaseOrderRequest.PurchaseItemRequest.builder().skuId(999999999L)
                                .quantity(1).unitPrice(new BigDecimal("100000")).build())).build();
        long items=purchaseItems.count();
        assertThrows(Exception.class,() -> purchaseService.create(req,f.employee().getId()));
        assertAll(() -> assertTrue(purchases.findBySupplierId(supplier.getId()).isEmpty(),"Partial purchase header remained committed"),
                () -> assertEquals(items,purchaseItems.count(),"First purchase line remained committed"));
    }

    private Long transfer(int firstQuantity, boolean addSecond) throws Exception {
        var items=new java.util.ArrayList<StockTransferRequest.TransferItemRequest>();
        items.add(StockTransferRequest.TransferItemRequest.builder().skuId(f.sku().getId()).quantity(firstQuantity).build());
        if(addSecond)items.add(StockTransferRequest.TransferItemRequest.builder().skuId(f.sku2().getId()).quantity(2).build());
        return transferService.create(StockTransferRequest.builder().sourceStoreId(f.hcm().getId())
                .destinationStoreId(f.hn().getId()).items(items).build(),f.employee().getId()).getId();
    }

    @Test @DisplayName("QA-INT-07: Shipping a transfer must preserve stock reserved for customer orders")
    void transferMustRespectHeldStock() throws Exception {
        stock(f.hcm().getId(),f.sku().getId(),10,8);
        Long id=transfer(5,false);
        Exception failure=null;
        try{transferService.confirmShipment(id);}catch(Exception e){failure=e;}
        Exception observed=failure;
        assertAll(() -> assertNotNull(observed,"Shipment succeeded although available stock is only 2"),
                () -> assertEquals(10,stock(f.hcm().getId(),f.sku().getId()).getActualStock(),"Held stock was consumed by transfer"));
    }

    @Test @DisplayName("QA-INT-08: Insufficient second transfer line must roll back deduction of first line")
    void transferMustRollbackAllLines() throws Exception {
        stock(f.hcm().getId(),f.sku().getId(),10,0);
        Long id=transfer(2,true);
        assertThrows(Exception.class,() -> transferService.confirmShipment(id));
        assertAll(() -> assertEquals(10,stock(f.hcm().getId(),f.sku().getId()).getActualStock(),"First deduction remained committed"),
                () -> assertEquals(TransferStatus.PENDING,transfers.findById(id).orElseThrow().getStatus()));
    }

    @Test @DisplayName("QA-INT-09: Wrong reset/registration OTP attempts must survive transaction rollback")
    void otpFailedAttemptMustPersist() {
        var reset=resetOtps.save(PasswordResetOtpEntity.builder().account(f.accountA()).identifier(f.accountA().getEmail())
                .otpCode("654321").expiryTime(LocalDateTime.now().plusMinutes(5)).failedAttempts(0).used(false).build());
        String email=f.tag()+"-register@example.test";
        var registration=registrationOtps.save(RegistrationOtpEntity.builder().email(email)
                .otpCode("654321").expiryTime(LocalDateTime.now().plusMinutes(5)).failedAttempts(0).used(false).build());
        assertThrows(BusinessException.class,() -> otpService.verifyOtp(VerifyOtpRequest.builder()
                .emailOrPhone(f.accountA().getEmail()).otp("111111").build()));
        assertThrows(BusinessException.class,() -> otpService.verifyRegistrationOtp(email,"111111"));
        assertAll(() -> assertEquals(1,resetOtps.findById(reset.getId()).orElseThrow().getFailedAttempts(),"Reset counter was rolled back"),
                () -> assertEquals(1,registrationOtps.findById(registration.getId()).orElseThrow().getFailedAttempts(),"Registration counter was rolled back"));
    }

    @Test @DisplayName("QA-INT-10: Public send-registration-OTP response must not return the OTP itself")
    void publicResponseMustNotExposeOtp() throws Exception {
        var result=mvc.perform(post("/api/auth/register/send-otp").header("X-Requested-With","fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"emailOrPhone\":\""+f.tag()+"-public@example.test\"}")).andReturn();
        assertEquals(200,result.getResponse().getStatus());
        String body=result.getResponse().getContentAsString();
        assertFalse(body.matches("(?s).*\"debugOtp\"\\s*:\\s*\"[0-9]{6}\".*"),"Public response contains a live debugOtp (value redacted)");
    }

    @Test @DisplayName("QA-INT-11: HCM warehouse employee must not adjust HN stock by changing storeId")
    void inventoryMustEnforceStoreScope() throws Exception {
        String body="{\"storeId\":"+f.hn().getId()+",\"skuId\":"+f.sku().getId()+",\"newQuantity\":33,\"reason\":\"QA cross-store probe\"}";
        var result=mvc.perform(post("/api/inventory/adjust").with(employee("INVENTORY_VIEW","INVENTORY_ADJUST"))
                .header("X-Requested-With","fetch").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        assertAll(() -> assertEquals(403,result.getResponse().getStatus(),"Cross-store adjustment must be denied"),
                () -> assertEquals(10,stock(f.hn().getId(),f.sku().getId()).getActualStock(),"HN stock was modified by HCM employee"));
    }

    @Test @DisplayName("QA-INT-12: Employee without ORDER_PROCESS must not update a same-store order via admin API")
    void adminOrderApiMustEnforceFunctionalPermission() throws Exception {
        var o=orderService.placeOrder(f.a().getId(),request(f.hcm().getId()));
        var result=mvc.perform(put("/api/admin/orders/"+o.getId()+"/status").param("status","CONFIRMED")
                .with(employee("INVENTORY_VIEW")).header("X-Requested-With","fetch")).andReturn();
        assertAll(() -> assertEquals(403,result.getResponse().getStatus(),"ROLE_EMPLOYEE alone must not authorize order processing"),
                () -> assertEquals(OrderStatus.PENDING,orders.findById(o.getId()).orElseThrow().getStatus()));
    }

    @Test @DisplayName("QA-INT-13: Order detail API denies an order assigned to another branch")
    void orderApiDeniesAnotherStore() throws Exception {
        var o=orderService.placeOrder(f.a().getId(),request(f.hn().getId()));
        var result=mvc.perform(get("/api/admin/orders/"+o.getId()).with(employee("ORDER_VIEW","ORDER_PROCESS"))).andReturn();
        assertEquals(403,result.getResponse().getStatus());
    }

    @Test @DisplayName("QA-INT-14: Customer A must not view B's order through the success-page orderCode")
    void successPageMustEnforceOrderOwnership() throws Exception {
        var o=orderService.placeOrder(f.b().getId(),request(f.hcm().getId()));
        var result=mvc.perform(get("/order-success").param("orderCode",o.getOrderCode()).with(customerA())).andReturn();
        Object exposed=result.getModelAndView()==null?null:result.getModelAndView().getModel().get("order");
        boolean leak=exposed instanceof OrderResponse response && !f.a().getId().equals(response.getCustomerId());
        assertFalse(leak,"Success page exposed another customer's order through orderCode");
    }

    @Test @DisplayName("QA-INT-15: Two transactions competing for the final unit must not create two HELD orders")
    void concurrentCheckoutMustNotOversellLastUnit() throws Exception {
        stock(f.hcm().getId(),f.sku().getId(),1,0);
        new TransactionTemplate(transactions).executeWithoutResult(tx -> {
            var item=cartItems.findByCartId(f.cartA().getId()).getFirst();item.setQuantity(1);cartItems.save(item);
        });
        var barrier=new CyclicBarrier(2);
        var executor=Executors.newFixedThreadPool(2);
        try {
            var futures=new java.util.ArrayList<Future<Boolean>>();
            for(Long customerId:List.of(f.a().getId(),f.b().getId())) {
                futures.add(executor.submit(() -> new TransactionTemplate(transactions).execute(tx -> {
                    // Synchronize a legal interleaving: both service transactions have
                    // observed the same final unit before either persists its hold.
                    stock(f.hcm().getId(),f.sku().getId());
                    try {barrier.await(20,TimeUnit.SECONDS);}
                    catch(Exception e){throw new IllegalStateException("QA barrier failed",e);}
                    try {orderService.placeOrder(customerId,request(f.hcm().getId()));return true;}
                    catch(Exception e){tx.setRollbackOnly();return false;}
                })));
            }
            int successes=0;
            for(var future:futures)if(Boolean.TRUE.equals(future.get(40,TimeUnit.SECONDS)))successes++;
            long holds=reservations.findAll().stream().filter(r -> r.getSku().getId().equals(f.sku().getId())
                    && r.getStatus()==ReservationStatus.HELD).mapToLong(OrderReservationEntity::getQuantity).sum();
            int completed=successes;
            assertAll(() -> assertEquals(1,completed,"Two transactions successfully sold the final unit"),
                    () -> assertEquals(1,holds,"HELD reservations exceed actual stock"));
        } finally {executor.shutdownNow();executor.awaitTermination(5,TimeUnit.SECONDS);}
    }
}
