package com.thinh.cosmetic.controller;

import tools.jackson.databind.ObjectMapper;
import com.thinh.cosmetic.domain.dto.request.account.EmployeeRequest;
import com.thinh.cosmetic.domain.entity.account.RoleEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.RoleRepository;
import com.thinh.cosmetic.repository.store.StoreRepository;
import com.thinh.cosmetic.security.AuthPrincipal;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoleSidebarAndVerificationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StoreRepository storeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RequestPostProcessor createPrincipal(String username, String displayName, Long empId, String... authorities) {
        List<SimpleGrantedAuthority> authList = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();

        AuthPrincipal principal = AuthPrincipal.builder()
                .accountId(empId != null ? empId + 100 : 999L)
                .employeeId(empId)
                .displayName(displayName)
                .username(username)
                .accountType(AccountType.EMPLOYEE)
                .authorities(authList)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "n/a", authList);
        return org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(auth);
    }

    // ─── 1. Thủ kho HCM (warehouse_hcm) ───

    @Test
    @DisplayName("Thủ kho: Truy cập /admin được tự động điều hướng sang /admin/inventory")
    void warehouseStaff_redirectsToInventory() throws Exception {
        mockMvc.perform(get("/admin")
                        .with(createPrincipal("warehouse_hcm", "Phạm Văn Kho", 4L,
                                "ROLE_EMPLOYEE", "ROLE_WAREHOUSE_STAFF", "WAREHOUSE_STAFF", "INVENTORY_VIEW", "INVENTORY_ADJUST")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/inventory"));
    }

    @Test
    @DisplayName("Thủ kho: Truy cập /admin/inventory thành công (200 OK) và hiển thị đúng chức danh, chi nhánh")
    void warehouseStaff_accessInventorySuccess() throws Exception {
        mockMvc.perform(get("/admin/inventory")
                        .with(createPrincipal("warehouse_hcm", "Phạm Văn Kho", 4L,
                                "ROLE_EMPLOYEE", "ROLE_WAREHOUSE_STAFF", "WAREHOUSE_STAFF", "INVENTORY_VIEW", "INVENTORY_ADJUST")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/inventory"))
                .andExpect(model().attribute("activeMenu", "inventory"))
                .andExpect(model().attribute("staffRoleTitle", "Quản Lý Kho / Thủ Kho"))
                .andExpect(model().attributeExists("primaryStoreName"));
    }

    @Test
    @DisplayName("Thủ kho: Gõ URL /admin/orders bị từ chối truy cập (403 Forbidden)")
    void warehouseStaff_ordersAccessDenied() throws Exception {
        mockMvc.perform(get("/admin/orders")
                        .with(createPrincipal("warehouse_hcm", "Phạm Văn Kho", 4L,
                                "ROLE_EMPLOYEE", "ROLE_WAREHOUSE_STAFF", "WAREHOUSE_STAFF", "INVENTORY_VIEW", "INVENTORY_ADJUST")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Thủ kho: Gõ URL /admin/employees bị từ chối truy cập (403 Forbidden)")
    void warehouseStaff_employeesAccessDenied() throws Exception {
        mockMvc.perform(get("/admin/employees")
                        .with(createPrincipal("warehouse_hcm", "Phạm Văn Kho", 4L,
                                "ROLE_EMPLOYEE", "ROLE_WAREHOUSE_STAFF", "WAREHOUSE_STAFF", "INVENTORY_VIEW", "INVENTORY_ADJUST")))
                .andExpect(status().isForbidden());
    }

    // ─── 2. Nhân viên đơn hàng (order_hcm) ───

    @Test
    @DisplayName("Nhân viên đơn hàng: Truy cập /admin tự chuyển vào /admin/orders")
    void orderStaff_redirectsToOrders() throws Exception {
        mockMvc.perform(get("/admin")
                        .with(createPrincipal("order_hcm", "Trần Minh Quân", 2L,
                                "ROLE_EMPLOYEE", "ROLE_ORDER_STAFF", "ORDER_STAFF", "ORDER_VIEW", "ORDER_PROCESS", "INVENTORY_VIEW")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));
    }

    @Test
    @DisplayName("Nhân viên đơn hàng: Truy cập /admin/orders và tra cứu /admin/inventory thành công")
    void orderStaff_canAccessOrdersAndInventory() throws Exception {
        // Vào đơn hàng
        mockMvc.perform(get("/admin/orders")
                        .with(createPrincipal("order_hcm", "Trần Minh Quân", 2L,
                                "ROLE_EMPLOYEE", "ROLE_ORDER_STAFF", "ORDER_STAFF", "ORDER_VIEW", "ORDER_PROCESS", "INVENTORY_VIEW")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orders"))
                .andExpect(model().attribute("staffRoleTitle", "Nhân Viên Đơn Hàng"));

        // Tra cứu tồn kho
        mockMvc.perform(get("/admin/inventory")
                        .with(createPrincipal("order_hcm", "Trần Minh Quân", 2L,
                                "ROLE_EMPLOYEE", "ROLE_ORDER_STAFF", "ORDER_STAFF", "ORDER_VIEW", "ORDER_PROCESS", "INVENTORY_VIEW")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/inventory"))
                .andExpect(model().attribute("staffRoleTitle", "Nhân Viên Đơn Hàng"));

        // Gõ /admin/employees bị chặn
        mockMvc.perform(get("/admin/employees")
                        .with(createPrincipal("order_hcm", "Trần Minh Quân", 2L,
                                "ROLE_EMPLOYEE", "ROLE_ORDER_STAFF", "ORDER_STAFF", "ORDER_VIEW", "ORDER_PROCESS", "INVENTORY_VIEW")))
                .andExpect(status().isForbidden());
    }

    // ─── 3. Quản trị viên (admin) ───

    @Test
    @DisplayName("Admin: Truy cập đầy đủ Tổng quan, Đơn hàng, Tồn kho, Sản phẩm và Nhân sự")
    void admin_canAccessAllPages() throws Exception {
        RequestPostProcessor adminPrincipal = createPrincipal("admin", "Nguyễn Quản Trị Hệ Thống", 1L,
                "ROLE_EMPLOYEE", "ROLE_ADMIN", "ADMIN", "ALL_STORES", "REPORT_VIEW", "EMPLOYEE_MANAGE", "ORDER_VIEW", "INVENTORY_VIEW");

        mockMvc.perform(get("/admin").with(adminPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attribute("staffRoleTitle", "Quản Trị Viên (Admin)"));

        mockMvc.perform(get("/admin/orders").with(adminPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orders"));

        mockMvc.perform(get("/admin/inventory").with(adminPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/inventory"));

        mockMvc.perform(get("/admin/products").with(adminPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products"));

        mockMvc.perform(get("/admin/employees").with(adminPrincipal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/employees"));
    }

    // ─── 4. Kịch bản Admin tạo Quản lý kho mới cho chi nhánh Hà Nội & Đăng nhập thử ───

    @Test
    @DisplayName("Admin: Mở form tạo Quản lý kho Hà Nội, gán quyền và đăng nhập thử bằng tài khoản mới")
    void admin_createHanoiWarehouseManager_andLoginSuccess() throws Exception {
        RequestPostProcessor adminPrincipal = createPrincipal("admin", "Nguyễn Quản Trị Hệ Thống", 1L,
                "ROLE_EMPLOYEE", "ROLE_ADMIN", "ADMIN", "ALL_STORES", "EMPLOYEE_MANAGE");

        RoleEntity warehouseRole = roleRepository.findByName("WAREHOUSE_STAFF").orElseThrow();
        List<StoreEntity> stores = storeRepository.findAll();
        StoreEntity hanoiStore = stores.stream()
                .filter(s -> s.getName() != null && s.getName().contains("Hà Nội"))
                .findFirst()
                .orElse(stores.get(stores.size() > 1 ? 1 : 0));

        String testUsername = "kho_hn_verify_" + System.currentTimeMillis();
        String testEmail = testUsername + "@lunea.test";

        EmployeeRequest createReq = EmployeeRequest.builder()
                .fullName("Lê Quản Kho Hà Nội")
                .username(testUsername)
                .email(testEmail)
                .internalEmail(testEmail)
                .phone("0908887766")
                .password("123456")
                .roleIds(List.of(warehouseRole.getId()))
                .storeIds(List.of(hanoiStore.getId()))
                .status(ActiveStatus.ACTIVE)
                .build();

        // 1. Admin gửi POST /api/employees tạo tài khoản
        mockMvc.perform(post("/api/employees")
                        .with(adminPrincipal)
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Lê Quản Kho Hà Nội"))
                .andExpect(jsonPath("$.username").value(testUsername));

        // 2. Đăng nhập thử bằng tài khoản mới tạo (identifier + password 123456)
        String loginPayload = String.format("{\"identifier\":\"%s\",\"password\":\"123456\"}", testUsername);
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountType").value("EMPLOYEE"))
                .andExpect(cookie().exists("LUNEA_AT"))
                .andReturn();

        Cookie authCookie = loginResult.getResponse().getCookie("LUNEA_AT");
        assertNotNull(authCookie, "Cookie LUNEA_AT phải được thiết lập");

        // 3. Nhân viên mới truy cập /admin -> chuyển hướng đến /admin/inventory
        mockMvc.perform(get("/admin").cookie(authCookie))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/inventory"));

        // 4. Nhân viên mới truy cập /admin/inventory -> 200 OK với chức danh và chi nhánh Hà Nội
        mockMvc.perform(get("/admin/inventory").cookie(authCookie))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/inventory"))
                .andExpect(model().attribute("staffRoleTitle", "Quản Lý Kho / Thủ Kho"))
                .andExpect(model().attribute("primaryStoreName", hanoiStore.getName()));

        // 5. Nhân viên mới thử vào /admin/orders và /admin/employees -> 403 Forbidden
        mockMvc.perform(get("/admin/orders").cookie(authCookie))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin/employees").cookie(authCookie))
                .andExpect(status().isForbidden());
    }
}
