package com.thinh.cosmetic.config;

import com.thinh.cosmetic.domain.entity.account.*;
import com.thinh.cosmetic.domain.entity.catalog.*;
import com.thinh.cosmetic.domain.entity.inventory.*;
import com.thinh.cosmetic.domain.entity.sales.CartEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.Gender;
import com.thinh.cosmetic.repository.account.*;
import com.thinh.cosmetic.repository.cart.CartRepository;
import com.thinh.cosmetic.repository.catalog.*;
import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final EmployeeStoreRepository employeeStoreRepository;
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CartRepository cartRepository;

    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductImageRepository productImageRepository;
    private final AttributeRepository attributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;
    private final InventoryRepository inventoryRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() > 0 && accountRepository.count() > 0 && storeRepository.count() > 0) {
            log.info(">>> [DataSeeder] Dữ liệu đã tồn tại trong SQL Server. Bỏ qua nạp dữ liệu mẫu.");
            return;
        }

        log.info(">>> [DataSeeder] Bắt đầu nạp dữ liệu mẫu hệ thống chuỗi cửa hàng mỹ phẩm LUNEA...");

        // 1. Phân quyền & Vai trò (Permissions & Roles)
        Map<String, PermissionEntity> perms = seedPermissions();
        Map<String, RoleEntity> roles = seedRoles(perms);

        // 2. Chi nhánh chuỗi cửa hàng (Stores)
        List<StoreEntity> stores = seedStores();
        StoreEntity storeHcm = stores.get(0);
        StoreEntity storeHn = stores.get(1);
        StoreEntity storeDn = stores.get(2);

        // 3. Tài khoản demo (Accounts & Employees & Customer)
        seedDemoAccounts(roles, storeHcm, storeHn);

        // 4. Thuộc tính làm đẹp (Attributes: Loại da & Nhu cầu)
        Map<String, AttributeEntity> attributes = seedAttributes();

        // 5. Thương hiệu & Danh mục (Brands & Categories)
        Map<String, BrandEntity> brands = seedBrands();
        Map<String, CategoryEntity> categories = seedCategories();

        // 6. Sản phẩm, Biến thể SKU, Thuộc tính, Ảnh & Tồn kho đa chi nhánh
        seedCatalogAndInventory(brands, categories, attributes, stores);

        log.info(">>> [DataSeeder] Hoàn thành nạp dữ liệu mẫu thành công vào Microsoft SQL Server!");
    }

    private Map<String, PermissionEntity> seedPermissions() {
        log.info(">>> [DataSeeder] Nạp danh sách Permissions...");
        List<PermissionEntity> list = List.of(
                PermissionEntity.builder().code("CATALOG_MANAGE").name("Quản lý danh mục & sản phẩm").description("Toàn quyền thêm/sửa/xóa sản phẩm và thương hiệu").build(),
                PermissionEntity.builder().code("ORDER_VIEW").name("Xem đơn hàng").description("Xem danh sách và chi tiết đơn hàng trong phạm vi chi nhánh").build(),
                PermissionEntity.builder().code("ORDER_PROCESS").name("Xử lý đơn hàng").description("Duyệt trạng thái đơn hàng, hủy đơn theo phạm vi chi nhánh").build(),
                PermissionEntity.builder().code("INVENTORY_VIEW").name("Tra cứu tồn kho").description("Xem tồn kho các sản phẩm theo chi nhánh").build(),
                PermissionEntity.builder().code("INVENTORY_ADJUST").name("Điều chỉnh tồn kho").description("Cập nhật số lượng tồn kho thực tế kèm lý do").build(),
                PermissionEntity.builder().code("STORE_MANAGE").name("Quản lý chi nhánh").description("Thêm, sửa thông tin các chi nhánh chuỗi cửa hàng").build(),
                PermissionEntity.builder().code("EMPLOYEE_MANAGE").name("Quản lý nhân viên").description("Quản lý tài khoản, phân vai trò và gán chi nhánh").build(),
                PermissionEntity.builder().code("VOUCHER_MANAGE").name("Quản lý voucher").description("Quản lý mã giảm giá khuyến mãi").build(),
                PermissionEntity.builder().code("PURCHASE_MANAGE").name("Quản lý nhập hàng").description("Quản lý nhà cung cấp và xác nhận phiếu nhập").build(),
                PermissionEntity.builder().code("CUSTOMER_MANAGE").name("Quản lý khách hàng").description("Xem và khóa/mở khóa tài khoản khách").build(),
                PermissionEntity.builder().code("REPORT_VIEW").name("Xem báo cáo thống kê").description("Xem dashboard doanh thu và biểu đồ đơn").build(),
                PermissionEntity.builder().code("AUDIT_VIEW").name("Xem nhật ký kiểm toán").description("Tra cứu lịch sử thao tác hệ thống").build()
        );

        Map<String, PermissionEntity> map = new HashMap<>();
        for (PermissionEntity p : list) {
            map.put(p.getCode(), permissionRepository.save(p));
        }
        return map;
    }

    private Map<String, RoleEntity> seedRoles(Map<String, PermissionEntity> perms) {
        log.info(">>> [DataSeeder] Nạp danh sách Roles...");
        RoleEntity adminRole = roleRepository.save(RoleEntity.builder().name("ADMIN").description("Quản trị viên toàn hệ thống").build());
        RoleEntity orderStaffRole = roleRepository.save(RoleEntity.builder().name("ORDER_STAFF").description("Nhân viên xử lý đơn hàng chi nhánh").build());
        RoleEntity warehouseRole = roleRepository.save(RoleEntity.builder().name("WAREHOUSE_STAFF").description("Nhân viên thủ kho chi nhánh").build());
        RoleEntity productManagerRole = roleRepository.save(RoleEntity.builder().name("PRODUCT_MANAGER").description("Quản lý danh mục sản phẩm").build());
        RoleEntity customerRole = roleRepository.save(RoleEntity.builder().name("CUSTOMER").description("Khách hàng mua sắm").build());

        // Admin có toàn bộ quyền
        for (PermissionEntity p : perms.values()) {
            rolePermissionRepository.save(RolePermissionEntity.builder().role(adminRole).permission(p).build());
        }

        // Order staff có quyền xem và xử lý đơn + xem tồn
        assignPerms(orderStaffRole, perms, "ORDER_VIEW", "ORDER_PROCESS", "INVENTORY_VIEW");

        // Warehouse staff có quyền xem tồn và điều chỉnh tồn
        assignPerms(warehouseRole, perms, "INVENTORY_VIEW", "INVENTORY_ADJUST");

        // Product manager có quyền catalog và xem tồn
        assignPerms(productManagerRole, perms, "CATALOG_MANAGE", "INVENTORY_VIEW");

        Map<String, RoleEntity> map = new HashMap<>();
        map.put("ADMIN", adminRole);
        map.put("ORDER_STAFF", orderStaffRole);
        map.put("WAREHOUSE_STAFF", warehouseRole);
        map.put("PRODUCT_MANAGER", productManagerRole);
        map.put("CUSTOMER", customerRole);
        return map;
    }

    private void assignPerms(RoleEntity role, Map<String, PermissionEntity> perms, String... codes) {
        for (String c : codes) {
            PermissionEntity p = perms.get(c);
            if (p != null) {
                rolePermissionRepository.save(RolePermissionEntity.builder().role(role).permission(p).build());
            }
        }
    }

    private List<StoreEntity> seedStores() {
        log.info(">>> [DataSeeder] Nạp 3 Chi nhánh chuỗi cửa hàng...");
        StoreEntity s1 = StoreEntity.builder()
                .name("LUNEA Đồng Khởi - Quận 1 (TP.HCM)")
                .address("123 Đường Đồng Khởi, Phường Bến Nghé, Quận 1")
                .province("Hồ Chí Minh")
                .phone("028.3822.9999")
                .openTime(LocalTime.of(8, 30))
                .closeTime(LocalTime.of(22, 0))
                .latitude(new BigDecimal("10.776889"))
                .longitude(new BigDecimal("106.700806"))
                .status(ActiveStatus.ACTIVE)
                .build();

        StoreEntity s2 = StoreEntity.builder()
                .name("LUNEA Cầu Giấy - Hà Nội")
                .address("68 Đường Cầu Giấy, Phường Quan Hoa, Quận Cầu Giấy")
                .province("Hà Nội")
                .phone("024.3766.8888")
                .openTime(LocalTime.of(8, 30))
                .closeTime(LocalTime.of(21, 30))
                .latitude(new BigDecimal("21.033333"))
                .longitude(new BigDecimal("105.792778"))
                .status(ActiveStatus.ACTIVE)
                .build();

        StoreEntity s3 = StoreEntity.builder()
                .name("LUNEA Bạch Đằng - Hải Châu (Đà Nẵng)")
                .address("186 Đường Bạch Đằng, Phường Hải Châu 1, Quận Hải Châu")
                .province("Đà Nẵng")
                .phone("0236.3888.777")
                .openTime(LocalTime.of(9, 0))
                .closeTime(LocalTime.of(21, 30))
                .latitude(new BigDecimal("16.067778"))
                .longitude(new BigDecimal("108.223889"))
                .status(ActiveStatus.ACTIVE)
                .build();

        return List.of(
                storeRepository.save(s1),
                storeRepository.save(s2),
                storeRepository.save(s3)
        );
    }

    private void seedDemoAccounts(Map<String, RoleEntity> roles, StoreEntity storeHcm, StoreEntity storeHn) {
        log.info(">>> [DataSeeder] Nạp tài khoản demo...");
        String defaultPasswordHash = passwordEncoder.encode("123456");

        // 1. Admin
        AccountEntity accAdmin = accountRepository.save(AccountEntity.builder()
                .username("admin")
                .email("admin@lunea.test")
                .phone("0900000001")
                .passwordHash(defaultPasswordHash)
                .accountType(AccountType.EMPLOYEE)
                .status(ActiveStatus.ACTIVE)
                .build());
        EmployeeEntity empAdmin = employeeRepository.save(EmployeeEntity.builder()
                .account(accAdmin)
                .fullName("Nguyễn Quản Trị Hệ Thống")
                .internalEmail("admin@lunea.test")
                .phone("0900000001")
                .status(ActiveStatus.ACTIVE)
                .build());
        employeeRoleRepository.save(EmployeeRoleEntity.builder().employee(empAdmin).role(roles.get("ADMIN")).build());
        employeeStoreRepository.save(EmployeeStoreEntity.builder().employee(empAdmin).store(storeHcm).isPrimary(true).build());

        // 2. Order Staff HCM
        AccountEntity accOrderHcm = accountRepository.save(AccountEntity.builder()
                .username("order_hcm")
                .email("order.hcm@lunea.test")
                .phone("0900000002")
                .passwordHash(defaultPasswordHash)
                .accountType(AccountType.EMPLOYEE)
                .status(ActiveStatus.ACTIVE)
                .build());
        EmployeeEntity empOrderHcm = employeeRepository.save(EmployeeEntity.builder()
                .account(accOrderHcm)
                .fullName("Trần Minh Quân (Đơn hàng HCM)")
                .internalEmail("order.hcm@lunea.test")
                .phone("0900000002")
                .status(ActiveStatus.ACTIVE)
                .build());
        employeeRoleRepository.save(EmployeeRoleEntity.builder().employee(empOrderHcm).role(roles.get("ORDER_STAFF")).build());
        employeeStoreRepository.save(EmployeeStoreEntity.builder().employee(empOrderHcm).store(storeHcm).isPrimary(true).build());

        // 3. Order Staff HN
        AccountEntity accOrderHn = accountRepository.save(AccountEntity.builder()
                .username("order_hn")
                .email("order.hn@lunea.test")
                .phone("0900000003")
                .passwordHash(defaultPasswordHash)
                .accountType(AccountType.EMPLOYEE)
                .status(ActiveStatus.ACTIVE)
                .build());
        EmployeeEntity empOrderHn = employeeRepository.save(EmployeeEntity.builder()
                .account(accOrderHn)
                .fullName("Lê Thu Hà (Đơn hàng Hà Nội)")
                .internalEmail("order.hn@lunea.test")
                .phone("0900000003")
                .status(ActiveStatus.ACTIVE)
                .build());
        employeeRoleRepository.save(EmployeeRoleEntity.builder().employee(empOrderHn).role(roles.get("ORDER_STAFF")).build());
        employeeStoreRepository.save(EmployeeStoreEntity.builder().employee(empOrderHn).store(storeHn).isPrimary(true).build());

        // 4. Warehouse Staff HCM
        AccountEntity accWarehouse = accountRepository.save(AccountEntity.builder()
                .username("warehouse_hcm")
                .email("warehouse.hcm@lunea.test")
                .phone("0900000004")
                .passwordHash(defaultPasswordHash)
                .accountType(AccountType.EMPLOYEE)
                .status(ActiveStatus.ACTIVE)
                .build());
        EmployeeEntity empWarehouse = employeeRepository.save(EmployeeEntity.builder()
                .account(accWarehouse)
                .fullName("Phạm Văn Kho (Thủ kho HCM)")
                .internalEmail("warehouse.hcm@lunea.test")
                .phone("0900000004")
                .status(ActiveStatus.ACTIVE)
                .build());
        employeeRoleRepository.save(EmployeeRoleEntity.builder().employee(empWarehouse).role(roles.get("WAREHOUSE_STAFF")).build());
        employeeStoreRepository.save(EmployeeStoreEntity.builder().employee(empWarehouse).store(storeHcm).isPrimary(true).build());

        // 5. Customer Demo
        AccountEntity accCustomer = accountRepository.save(AccountEntity.builder()
                .username("khach01@lunea.test")
                .email("khach01@lunea.test")
                .phone("0901234567")
                .passwordHash(defaultPasswordHash)
                .accountType(AccountType.CUSTOMER)
                .status(ActiveStatus.ACTIVE)
                .build());
        CustomerEntity customer = customerRepository.save(CustomerEntity.builder()
                .account(accCustomer)
                .fullName("Lê Hoàng Yến")
                .dob(LocalDate.of(1998, 5, 15))
                .gender(Gender.FEMALE)
                .loyaltyPoints(120)
                .joinDate(LocalDate.now().minusMonths(3))
                .build());

        customerAddressRepository.save(CustomerAddressEntity.builder()
                .customerEntity(customer)
                .recipientName("Lê Hoàng Yến")
                .phone("0901234567")
                .addressDetail("Số 45 Đường Lê Duẩn")
                .ward("Phường Bến Nghé")
                .district("Quận 1")
                .province("Hồ Chí Minh")
                .isDefault(true)
                .build());

        cartRepository.save(CartEntity.builder()
                .customer(customer)
                .updatedAt(LocalDateTime.now())
                .build());
    }

    private Map<String, AttributeEntity> seedAttributes() {
        log.info(">>> [DataSeeder] Nạp Attributes (Loại da & Nhu cầu)...");
        List<AttributeEntity> attrs = List.of(
                AttributeEntity.builder().name("Da Dầu").group("SKIN_TYPE").build(),
                AttributeEntity.builder().name("Da Khô").group("SKIN_TYPE").build(),
                AttributeEntity.builder().name("Da Nhạy Cảm").group("SKIN_TYPE").build(),
                AttributeEntity.builder().name("Da Hỗn Hợp").group("SKIN_TYPE").build(),
                AttributeEntity.builder().name("Trị Mụn").group("NEED").build(),
                AttributeEntity.builder().name("Phục Hồi Da").group("NEED").build(),
                AttributeEntity.builder().name("Dưỡng Sáng Mờ Thâm").group("NEED").build(),
                AttributeEntity.builder().name("Chống Lão Hóa").group("NEED").build(),
                AttributeEntity.builder().name("Dưỡng Ẩm Chuyên Sâu").group("NEED").build()
        );

        Map<String, AttributeEntity> map = new HashMap<>();
        for (AttributeEntity a : attrs) {
            map.put(a.getName(), attributeRepository.save(a));
        }
        return map;
    }

    private Map<String, BrandEntity> seedBrands() {
        log.info(">>> [DataSeeder] Nạp Thương hiệu mỹ phẩm...");
        List<BrandEntity> list = List.of(
                BrandEntity.builder().name("Cocoon Vietnam").description("Thương hiệu mỹ phẩm thuần chay 100% từ nguyên liệu thiên nhiên Việt Nam.").status(ActiveStatus.ACTIVE).build(),
                BrandEntity.builder().name("The Ordinary").description("Thương hiệu dưỡng da khoa học tối giản, nồng độ hoạt chất cao từ Canada.").status(ActiveStatus.ACTIVE).build(),
                BrandEntity.builder().name("La Roche-Posay").description("Dược mỹ phẩm hàng đầu từ Pháp, chuyên sâu chăm sóc làn da nhạy cảm.").status(ActiveStatus.ACTIVE).build(),
                BrandEntity.builder().name("Paula's Choice").description("Thương hiệu dược mỹ phẩm khoa học hàng đầu thế giới với BHA/Retinol.").status(ActiveStatus.ACTIVE).build(),
                BrandEntity.builder().name("Innisfree").description("Thương hiệu mỹ phẩm thiên nhiên từ đảo Jeju, Hàn Quốc.").status(ActiveStatus.ACTIVE).build()
        );

        Map<String, BrandEntity> map = new HashMap<>();
        for (BrandEntity b : list) {
            map.put(b.getName(), brandRepository.save(b));
        }
        return map;
    }

    private Map<String, CategoryEntity> seedCategories() {
        log.info(">>> [DataSeeder] Nạp Danh mục mỹ phẩm...");
        CategoryEntity cSkincare = categoryRepository.save(CategoryEntity.builder().name("Chăm sóc da").description("Sản phẩm dưỡng da mặt chuyên sâu").status(ActiveStatus.ACTIVE).build());
        CategoryEntity cMakeup = categoryRepository.save(CategoryEntity.builder().name("Trang điểm").description("Mỹ phẩm trang điểm cao cấp").status(ActiveStatus.ACTIVE).build());
        CategoryEntity cBodyHair = categoryRepository.save(CategoryEntity.builder().name("Chăm sóc tóc & Cơ thể").description("Sản phẩm chăm sóc toàn thân").status(ActiveStatus.ACTIVE).build());

        CategoryEntity cSerum = categoryRepository.save(CategoryEntity.builder().name("Tinh chất & Serum").parent(cSkincare).status(ActiveStatus.ACTIVE).build());
        CategoryEntity cCream = categoryRepository.save(CategoryEntity.builder().name("Kem dưỡng ẩm").parent(cSkincare).status(ActiveStatus.ACTIVE).build());
        CategoryEntity cCleanser = categoryRepository.save(CategoryEntity.builder().name("Sữa rửa mặt").parent(cSkincare).status(ActiveStatus.ACTIVE).build());
        CategoryEntity cSunscreen = categoryRepository.save(CategoryEntity.builder().name("Kem chống nắng").parent(cSkincare).status(ActiveStatus.ACTIVE).build());
        CategoryEntity cLipstick = categoryRepository.save(CategoryEntity.builder().name("Son môi").parent(cMakeup).status(ActiveStatus.ACTIVE).build());

        Map<String, CategoryEntity> map = new HashMap<>();
        map.put("SKINCARE", cSkincare);
        map.put("SERUM", cSerum);
        map.put("CREAM", cCream);
        map.put("CLEANSER", cCleanser);
        map.put("SUNSCREEN", cSunscreen);
        map.put("LIPSTICK", cLipstick);
        map.put("BODYHAIR", cBodyHair);
        return map;
    }

    private void seedCatalogAndInventory(
            Map<String, BrandEntity> brands,
            Map<String, CategoryEntity> categories,
            Map<String, AttributeEntity> attrs,
            List<StoreEntity> stores
    ) {
        log.info(">>> [DataSeeder] Nạp Sản phẩm, Biến thể SKU, Ảnh & Tồn kho...");

        StoreEntity storeHcm = stores.get(0);
        StoreEntity storeHn = stores.get(1);
        StoreEntity storeDn = stores.get(2);

        // Product 1: Serum Bí Đao Cocoon (Đủ hàng ở 3 chi nhánh)
        ProductEntity p1 = createProduct(
                "Serum Bí Đao Cocoon 7% Niacinamide Trị Mụn & Mờ Thâm",
                brands.get("Cocoon Vietnam"),
                categories.get("SERUM"),
                "Việt Nam",
                "Bí đao, Rau má, Tràm trà, 7% Niacinamide",
                "Giúp kiểm soát dầu thừa, làm dịu vết mụn đỏ, làm mờ vết thâm mụn rõ rệt.",
                "Serum bí đao Cocoon với sự kết hợp của dịch chiết bí đao, rau má và tràm trà giúp kháng khuẩn, làm thông thoáng lỗ chân lông.",
                true
        );
        ProductSkuEntity p1Sku1 = createSku(p1, "SKU-CCN-BD-70ML", "Dung tích 70ml", new BigDecimal("265000"), new BigDecimal("295000"), "893851500001");
        ProductSkuEntity p1Sku2 = createSku(p1, "SKU-CCN-BD-140ML", "Dung tích 140ml", new BigDecimal("465000"), new BigDecimal("520000"), "893851500002");
        createImage(p1, p1Sku1, "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80", true);
        createImage(p1, null, "https://images.unsplash.com/photo-1608248597359-598d9cb62118?w=600&auto=format&fit=crop&q=80", false);
        linkAttributes(p1, attrs, "Da Dầu", "Da Mụn", "Trị Mụn", "Dưỡng Sáng Mờ Thâm");
        createInventory(storeHcm, p1Sku1, 30, 0);
        createInventory(storeHn, p1Sku1, 20, 0);
        createInventory(storeDn, p1Sku1, 15, 0);
        createInventory(storeHcm, p1Sku2, 20, 0);
        createInventory(storeHn, p1Sku2, 10, 0);
        createInventory(storeDn, p1Sku2, 5, 0);

        // Product 2: The Ordinary Niacinamide (HẾT HÀNG TOÀN CHUỖI để demo "Tạm hết hàng" - Bước 1)
        ProductEntity p2 = createProduct(
                "Tinh Chất The Ordinary Niacinamide 10% + Zinc 1%",
                brands.get("The Ordinary"),
                categories.get("SERUM"),
                "Canada",
                "10% Niacinamide, 1% Zinc PCA",
                "Cân bằng bã nhờn, thu nhỏ lỗ chân lông và làm đều màu da.",
                "Công thức chứa vitamin B3 nồng độ cao kết hợp kẽm khoáng giúp thanh lọc da, giảm bít tắc tuyến bã nhờn.",
                true
        );
        ProductSkuEntity p2Sku1 = createSku(p2, "SKU-TO-NIA-30ML", "Dung tích 30ml", new BigDecimal("220000"), new BigDecimal("250000"), "769915190011");
        ProductSkuEntity p2Sku2 = createSku(p2, "SKU-TO-NIA-60ML", "Dung tích 60ml", new BigDecimal("380000"), new BigDecimal("430000"), "769915190012");
        createImage(p2, p2Sku1, "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p2, attrs, "Da Dầu", "Da Hỗn Hợp", "Trị Mụn", "Dưỡng Sáng Mờ Thâm");
        // Tồn kho = 0 ở mọi chi nhánh
        createInventory(storeHcm, p2Sku1, 0, 0);
        createInventory(storeHn, p2Sku1, 0, 0);
        createInventory(storeDn, p2Sku1, 0, 0);
        createInventory(storeHcm, p2Sku2, 0, 0);
        createInventory(storeHn, p2Sku2, 0, 0);
        createInventory(storeDn, p2Sku2, 0, 0);

        // Product 3: Serum La Roche-Posay Hyalu B5 (CHỈ CÒN Ở HCM để demo hệ thống tự gán chi nhánh HCM - Bước 10)
        ProductEntity p3 = createProduct(
                "Serum Phục Hồi Tái Tạo Da La Roche-Posay Hyalu B5",
                brands.get("La Roche-Posay"),
                categories.get("SERUM"),
                "Pháp",
                "Vitamin B5 (Panthenol), Dual Hyaluronic Acid, Madecassoside",
                "Cấp ẩm chuyên sâu, phục hồi hàng rào bảo vệ da, làm dịu da kích ứng.",
                "Dưỡng chất chuyên sâu với phức hợp Hyaluronic Acid đa phân tử và Vitamin B5 đậm đặc giúp làm đầy nếp nhăn và tái tạo làn da mệt mỏi.",
                true
        );
        ProductSkuEntity p3Sku1 = createSku(p3, "SKU-LRP-B5-30ML", "Dung tích 30ml", new BigDecimal("980000"), new BigDecimal("1150000"), "3337875583626");
        ProductSkuEntity p3Sku2 = createSku(p3, "SKU-LRP-B5-50ML", "Dung tích 50ml", new BigDecimal("1450000"), new BigDecimal("1650000"), "3337875583633");
        createImage(p3, p3Sku1, "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p3, attrs, "Da Nhạy Cảm", "Da Khô", "Phục Hồi Da", "Dưỡng Ẩm Chuyên Sâu");
        // Chỉ HCM còn hàng, HN và DN = 0
        createInventory(storeHcm, p3Sku1, 25, 0);
        createInventory(storeHn, p3Sku1, 0, 0);
        createInventory(storeDn, p3Sku1, 0, 0);
        createInventory(storeHcm, p3Sku2, 15, 0);
        createInventory(storeHn, p3Sku2, 0, 0);
        createInventory(storeDn, p3Sku2, 0, 0);

        // Product 4: Paula's Choice BHA 2% Liquid Exfoliant
        ProductEntity p4 = createProduct(
                "Dung Dịch Tẩy Tế Bào Chết Hóa Học Paula's Choice BHA 2% Liquid",
                brands.get("Paula's Choice"),
                categories.get("SERUM"),
                "Mỹ",
                "2% Salicylic Acid (BHA), Chiết xuất Trà xanh",
                "Loại bỏ tế bào chết sâu trong lỗ chân lông, làm mịn da thô ráp.",
                "Sản phẩm kinh điển chứa 2% BHA tan trong dầu giúp loại bỏ mụn đầu đen, bã nhờn tích tụ, đem lại bề mặt da mịn màng.",
                true
        );
        ProductSkuEntity p4Sku1 = createSku(p4, "SKU-PC-BHA-30ML", "Dung tích 30ml (Trial)", new BigDecimal("399000"), new BigDecimal("450000"), "065543902011");
        ProductSkuEntity p4Sku2 = createSku(p4, "SKU-PC-BHA-118ML", "Dung tích 118ml (Fullsize)", new BigDecimal("949000"), new BigDecimal("1050000"), "065543902012");
        createImage(p4, p4Sku1, "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p4, attrs, "Da Dầu", "Da Hỗn Hợp", "Trị Mụn");
        createInventory(storeHcm, p4Sku1, 40, 0);
        createInventory(storeHn, p4Sku1, 25, 0);
        createInventory(storeDn, p4Sku1, 20, 0);
        createInventory(storeHcm, p4Sku2, 30, 0);
        createInventory(storeHn, p4Sku2, 15, 0);
        createInventory(storeDn, p4Sku2, 10, 0);

        // Product 5: Kem Dưỡng Phục Hồi Cicaplast Baume B5+
        ProductEntity p5 = createProduct(
                "Kem Dưỡng Ẩm Phục Hồi Da La Roche-Posay Cicaplast Baume B5+",
                brands.get("La Roche-Posay"),
                categories.get("CREAM"),
                "Pháp",
                "Tribioma, 5% Panthenol, Madecassoside, Zinc",
                "Làm dịu tức thì các vết đỏ, ngứa ngáy, phục hồi màng ẩm sau 1 giờ.",
                "Kem dưỡng ẩm làm dịu và phục hồi da đa năng dành cho cả gia đình, phù hợp cho da sau điều trị mụn, kích ứng hoặc laser.",
                true
        );
        ProductSkuEntity p5Sku1 = createSku(p5, "SKU-LRP-CICA-40ML", "Dung tích 40ml", new BigDecimal("365000"), new BigDecimal("410000"), "3337875816809");
        ProductSkuEntity p5Sku2 = createSku(p5, "SKU-LRP-CICA-100ML", "Dung tích 100ml", new BigDecimal("620000"), new BigDecimal("690000"), "3337875816816");
        createImage(p5, p5Sku1, "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p5, attrs, "Da Nhạy Cảm", "Da Khô", "Phục Hồi Da", "Dưỡng Ẩm Chuyên Sâu");
        createInventory(storeHcm, p5Sku1, 50, 0);
        createInventory(storeHn, p5Sku1, 35, 0);
        createInventory(storeDn, p5Sku1, 25, 0);
        createInventory(storeHcm, p5Sku2, 30, 0);
        createInventory(storeHn, p5Sku2, 20, 0);
        createInventory(storeDn, p5Sku2, 15, 0);

        // Product 6: Kem Chống Nắng La Roche-Posay UVMune 400
        ProductEntity p6 = createProduct(
                "Kem Chống Nắng Kiềm Dầu La Roche-Posay Anthelios UVMune 400 Oil Control",
                brands.get("La Roche-Posay"),
                categories.get("SUNSCREEN"),
                "Pháp",
                "Màng lọc Mexoryl 400, Công nghệ Airlicium",
                "Bảo vệ toàn diện trước tia UVA dài, kiềm dầu đến 12 giờ không bóng nhờn.",
                "Đột phá màng lọc Mexoryl 400 bảo vệ da trước tia UVA sâu nhất (380-400nm), kết cấu gel lỏng thấm nhanh ráo mịn.",
                true
        );
        ProductSkuEntity p6Sku1 = createSku(p6, "SKU-LRP-SUN-50ML", "Dung tích 50ml", new BigDecimal("475000"), new BigDecimal("535000"), "3337875797641");
        createImage(p6, p6Sku1, "https://images.unsplash.com/photo-1556228722-d0b633519c67?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p6, attrs, "Da Dầu", "Da Hỗn Hợp", "Chống Lão Hóa");
        createInventory(storeHcm, p6Sku1, 60, 0);
        createInventory(storeHn, p6Sku1, 40, 0);
        createInventory(storeDn, p6Sku1, 30, 0);

        // Product 7: Sữa Rửa Mặt Cà Phê Đắk Lắk Cocoon
        ProductEntity p7 = createProduct(
                "Sữa Rửa Mặt Dịu Nhẹ Cocoon Cà Phê Đắk Lắk Sạch Sâu",
                brands.get("Cocoon Vietnam"),
                categories.get("CLEANSER"),
                "Việt Nam",
                "Chiết xuất cà phê Đắk Lắk, Dầu mù u, Axit Amin",
                "Làm sạch nhẹ dịu, không gây khô căng, đánh thức làn da tràn đầy năng lượng.",
                "Công thức pH 5.5 cân bằng với bọt mịn giúp cuốn trôi bụi bẩn và dầu thừa mà vẫn bảo tồn màng ẩm tự nhiên của da.",
                false
        );
        ProductSkuEntity p7Sku1 = createSku(p7, "SKU-CCN-SRM-140ML", "Chai 140ml", new BigDecimal("175000"), new BigDecimal("195000"), "893851500003");
        createImage(p7, p7Sku1, "https://images.unsplash.com/photo-1556228724-4da94812f862?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p7, attrs, "Da Dầu", "Da Hỗn Hợp", "Dưỡng Sáng Mờ Thâm");
        createInventory(storeHcm, p7Sku1, 45, 0);
        createInventory(storeHn, p7Sku1, 30, 0);
        createInventory(storeDn, p7Sku1, 20, 0);

        // Product 8: Nước Dưỡng Tóc Tinh Dầu Bưởi Cocoon
        ProductEntity p8 = createProduct(
                "Nước Dưỡng Tóc Tinh Dầu Bưởi Cocoon Pomelo Hair Tonic",
                brands.get("Cocoon Vietnam"),
                categories.get("BODYHAIR"),
                "Việt Nam",
                "Tinh dầu bưởi nguyên chất, Xylishine, Vitamin B5",
                "Giảm gãy rụng rõ rệt, kích thích mọc tóc con và nuôi dưỡng nang tóc chắc khỏe.",
                "Giải pháp thuần chay giải cứu mái tóc yếu gãy rụng, mùi hương hoa bưởi thanh khiết sảng khoái.",
                false
        );
        ProductSkuEntity p8Sku1 = createSku(p8, "SKU-CCN-TOC-140ML", "Chai xịt 140ml", new BigDecimal("145000"), new BigDecimal("165000"), "893851500004");
        createImage(p8, p8Sku1, "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&auto=format&fit=crop&q=80", true);
        createInventory(storeHcm, p8Sku1, 50, 0);
        createInventory(storeHn, p8Sku1, 35, 0);
        createInventory(storeDn, p8Sku1, 25, 0);

        // Product 9: Innisfree Green Tea Seed Hyaluronic Serum
        ProductEntity p9 = createProduct(
                "Serum Cấp Ẩm Chuyên Sâu Innisfree Green Tea Seed Hyaluronic",
                brands.get("Innisfree"),
                categories.get("SERUM"),
                "Hàn Quốc",
                "Trà xanh hữu cơ Beauty Green Tea, 5 loại Hyaluronic Acid",
                "Cấp nước tức thì, cải thiện tình trạng da thiếu ẩm từ sâu bên trong.",
                "Công nghệ thẩm thấu nano giúp dưỡng chất từ mầm trà xanh đảo Jeju khóa ẩm suốt 24 giờ cho làn da căng mọng.",
                false
        );
        ProductSkuEntity p9Sku1 = createSku(p9, "SKU-INN-TEA-80ML", "Dung tích 80ml", new BigDecimal("610000"), new BigDecimal("680000"), "8809612850011");
        createImage(p9, p9Sku1, "https://images.unsplash.com/photo-1535585209827-a15fcdbc4c2d?w=600&auto=format&fit=crop&q=80", true);
        linkAttributes(p9, attrs, "Da Khô", "Da Hỗn Hợp", "Dưỡng Ẩm Chuyên Sâu");
        createInventory(storeHcm, p9Sku1, 30, 0);
        createInventory(storeHn, p9Sku1, 20, 0);
        createInventory(storeDn, p9Sku1, 15, 0);

        // Product 10: Innisfree Matte Velvet Tint
        ProductEntity p10 = createProduct(
                "Son Kem Lì Mịn Môi Cao Cấp Innisfree Matte Velvet Tint",
                brands.get("Innisfree"),
                categories.get("LIPSTICK"),
                "Hàn Quốc",
                "Dầu jojoba hữu cơ, sắc tố màu khoáng",
                "Màu son lên chuẩn sắc, chất son nhẹ tựa mây không gây khô vân môi.",
                "Chất son nhung mờ mịn màng lâu trôi đến 8 giờ, cho đôi môi vẻ đẹp cuốn hút tự nhiên.",
                false
        );
        ProductSkuEntity p10Sku1 = createSku(p10, "SKU-INN-LIP-01", "Màu 01 - Đỏ Cherry Rực Rỡ", new BigDecimal("280000"), new BigDecimal("320000"), "8809612850021");
        ProductSkuEntity p10Sku2 = createSku(p10, "SKU-INN-LIP-02", "Màu 02 - Cam Đất Thời Thượng", new BigDecimal("280000"), new BigDecimal("320000"), "8809612850022");
        createImage(p10, p10Sku1, "https://images.unsplash.com/photo-1586495777744-4413f21062fa?w=600&auto=format&fit=crop&q=80", true);
        createInventory(storeHcm, p10Sku1, 25, 0);
        createInventory(storeHn, p10Sku1, 20, 0);
        createInventory(storeDn, p10Sku1, 15, 0);
        createInventory(storeHcm, p10Sku2, 30, 0);
        createInventory(storeHn, p10Sku2, 25, 0);
        createInventory(storeDn, p10Sku2, 10, 0);
    }

    private ProductEntity createProduct(
            String name, BrandEntity brand, CategoryEntity category,
            String origin, String ingredients, String uses, String description, boolean featured
    ) {
        return productRepository.save(ProductEntity.builder()
                .name(name)
                .brand(brand)
                .category(category)
                .origin(origin)
                .mainIngredients(ingredients)
                .uses(uses)
                .description(description)
                .isFeatured(featured)
                .status(ActiveStatus.ACTIVE)
                .build());
    }

    private ProductSkuEntity createSku(
            ProductEntity product, String skuCode, String variantName,
            BigDecimal price, BigDecimal listPrice, String barcode
    ) {
        return productSkuRepository.save(ProductSkuEntity.builder()
                .product(product)
                .skuCode(skuCode)
                .variantName(variantName)
                .price(price)
                .listPrice(listPrice)
                .barcode(barcode)
                .status(ActiveStatus.ACTIVE)
                .build());
    }

    private void createImage(ProductEntity product, ProductSkuEntity sku, String url, boolean isPrimary) {
        productImageRepository.save(ProductImageEntity.builder()
                .product(product)
                .productSku(sku)
                .imageUrl(url)
                .isPrimary(isPrimary)
                .build());
    }

    private void linkAttributes(ProductEntity product, Map<String, AttributeEntity> attrs, String... names) {
        for (String name : names) {
            AttributeEntity a = attrs.get(name);
            if (a != null) {
                productAttributeValueRepository.save(ProductAttributeValueEntity.builder()
                        .product(product)
                        .attribute(a)
                        .value(a.getName())
                        .build());
            }
        }
    }

    private void createInventory(StoreEntity store, ProductSkuEntity sku, int actual, int held) {
        inventoryRepository.save(InventoryEntity.builder()
                .store(store)
                .sku(sku)
                .actualStock(actual)
                .heldQuantity(held)
                .minimumStock(5)
                .updatedAt(LocalDateTime.now())
                .build());
    }
}
