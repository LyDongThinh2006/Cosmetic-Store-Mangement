package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.security.AuthPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    private static RequestPostProcessor authPrincipal(Long customerId, Long employeeId, AccountType type, String... authorities) {
        List<SimpleGrantedAuthority> authList = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();

        AuthPrincipal principal = AuthPrincipal.builder()
                .accountId(100L)
                .customerId(customerId)
                .employeeId(employeeId)
                .displayName("Test User")
                .username("testuser")
                .accountType(type)
                .authorities(authList)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "n/a", authList);
        return org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(auth);
    }

    // ─── 1. Anonymous Access to Protected APIs (Expect 401) ───

    @Test
    @DisplayName("Anonymous: Truy cập API danh sách nhân viên trả về 401 Unauthorized")
    void anonymous_cannotAccessEmployees() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Anonymous: Tạo sản phẩm trả về 401 Unauthorized")
    void anonymous_cannotCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Son môi\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Anonymous: Xem danh sách khách hàng trả về 401 Unauthorized")
    void anonymous_cannotAccessCustomers() throws Exception {
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Anonymous: Xem danh sách nhập hàng trả về 401 Unauthorized")
    void anonymous_cannotAccessPurchaseOrders() throws Exception {
        mockMvc.perform(get("/api/purchase-orders"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Anonymous: Xem danh sách điều chuyển kho trả về 401 Unauthorized")
    void anonymous_cannotAccessStockTransfers() throws Exception {
        mockMvc.perform(get("/api/stock-transfers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Anonymous: Xem tồn kho chi nhánh trả về 401 Unauthorized")
    void anonymous_cannotAccessStoreInventory() throws Exception {
        mockMvc.perform(get("/api/inventory/store/1"))
                .andExpect(status().isUnauthorized());
    }

    // ─── 2. Public Catalog APIs Accessible Anonymously (Expect 200) ───

    @Test
    @DisplayName("Public: Tra cứu danh sách sản phẩm thành công 200 không cần token")
    void public_canAccessProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public: Tra cứu danh sách danh mục thành công 200 không cần token")
    void public_canAccessCategories() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public: Tra cứu danh sách thương hiệu thành công 200 không cần token")
    void public_canAccessBrands() throws Exception {
        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Public: Tra cứu danh sách cửa hàng showroom thành công 200 không cần token")
    void public_canAccessStores() throws Exception {
        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk());
    }

    // ─── 3. Customer Role Blocked from Employee / Admin APIs (Expect 403) ───

    @Test
    @DisplayName("Customer: Không được phép truy cập danh sách nhân viên (403 Forbidden)")
    void customer_cannotAccessEmployees() throws Exception {
        mockMvc.perform(get("/api/employees")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Customer: Không được phép tạo sản phẩm (403 Forbidden)")
    void customer_cannotCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Kem dưỡng\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Customer: Không được phép xem tồn kho chi nhánh (403 Forbidden)")
    void customer_cannotViewInventory() throws Exception {
        mockMvc.perform(get("/api/inventory/store/1")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Customer: Không được phép duyệt đánh giá (403 Forbidden)")
    void customer_cannotModerateReview() throws Exception {
        mockMvc.perform(put("/api/reviews/1/status")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .param("status", "APPROVED"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Customer: Không được phép xem tất cả yêu cầu đổi trả (403 Forbidden)")
    void customer_cannotViewAllReturns() throws Exception {
        mockMvc.perform(get("/api/returns")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    // ─── 4. Customer Ownership Enforcement ───

    @Test
    @DisplayName("Ownership: Khách hàng 1 không thể xem hồ sơ khách hàng 2 (403 Forbidden)")
    void customer_cannotViewOtherCustomerProfile() throws Exception {
        mockMvc.perform(get("/api/customers/2")
                        .with(authPrincipal(1L, null, AccountType.CUSTOMER, "ROLE_CUSTOMER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Ownership: Khách hàng 1 xem được hồ sơ của chính mình (200 OK)")
    void customer_canViewOwnProfile() throws Exception {
        mockMvc.perform(get("/api/customers/1")
                        .with(authPrincipal(1L, null, AccountType.CUSTOMER, "ROLE_CUSTOMER")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Ownership: Khách hàng 1 không thể xem danh sách đổi trả của khách hàng 2 (403 Forbidden)")
    void customer_cannotViewOtherCustomerReturns() throws Exception {
        mockMvc.perform(get("/api/returns/customer/2")
                        .with(authPrincipal(1L, null, AccountType.CUSTOMER, "ROLE_CUSTOMER")))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Ownership: Khách hàng 1 xem được danh sách đổi trả của chính mình (200 OK)")
    void customer_canViewOwnReturns() throws Exception {
        mockMvc.perform(get("/api/returns/customer/1")
                        .with(authPrincipal(1L, null, AccountType.CUSTOMER, "ROLE_CUSTOMER")))
                .andExpect(status().isOk());
    }

    // ─── 5. Employee Permission & PreAuthorize Enforcement ───

    @Test
    @DisplayName("Permission: Nhân viên thiếu CATALOG_MANAGE bị 403 khi xóa sản phẩm")
    void employeeWithoutCatalogManage_cannotDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                        .with(authPrincipal(null, 2L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE"))
                        .header("X-Requested-With", "XMLHttpRequest"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Permission: Nhân viên thiếu CATALOG_MANAGE bị 403 khi tạo sản phẩm")
    void employeeWithoutCatalogManage_cannotCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .with(authPrincipal(null, 2L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE"))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brandId\":1,\"categoryId\":1,\"name\":\"Son môi Lunea Test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Permission: Nhân viên có CATALOG_MANAGE được phép tạo sản phẩm thành công (201 Created)")
    void employeeWithCatalogManage_canCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE", "CATALOG_MANAGE"))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brandId\":1,\"categoryId\":1,\"name\":\"Son môi Lunea Auth Test\",\"status\":\"ACTIVE\",\"isFeatured\":false}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Permission: Nhân viên có CUSTOMER_MANAGE xem được danh sách tất cả khách hàng (200 OK)")
    void employeeWithCustomerManage_canViewAllCustomers() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE", "CUSTOMER_MANAGE")))
                .andExpect(status().isOk());
    }
}
