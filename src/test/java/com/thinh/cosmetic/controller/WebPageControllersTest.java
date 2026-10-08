package com.thinh.cosmetic.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WebPageControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Public: Trang chủ trả về 200 và view 'home'")
    void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("pageTitle", "activeNav"));
    }

    @Test
    @DisplayName("Public: Trang cửa hàng và thương hiệu trả về 200")
    void testStoresAndBrandsPages() throws Exception {
        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(view().name("stores"))
                .andExpect(model().attribute("activeNav", "stores"));

        mockMvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(view().name("brands"))
                .andExpect(model().attribute("activeNav", "brands"));
    }

    @Test
    @DisplayName("Public: Danh sách và chi tiết sản phẩm trả về 200")
    void testProductPages() throws Exception {
        mockMvc.perform(get("/products")
                        .param("keyword", "serum")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-list"))
                .andExpect(model().attribute("keyword", "serum"))
                .andExpect(model().attribute("brandId", 1L));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attribute("productId", 1L));
    }

    @Test
    @DisplayName("Public: Trang login và register trả về 200")
    void testAuthPages() throws Exception {
        mockMvc.perform(get("/login").param("redirect", "/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"))
                .andExpect(model().attribute("redirect", "/checkout"));

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }

    @Test
    @DisplayName("Protected: Giỏ hàng chưa đăng nhập redirect sang /login")
    void testCartUnauthenticatedRedirects() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Customer: Giỏ hàng và Thanh toán trả về 200 cho ROLE_CUSTOMER")
    void testCartAndCheckoutForCustomer() throws Exception {
        mockMvc.perform(get("/cart")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));

        mockMvc.perform(get("/checkout")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"));

        mockMvc.perform(get("/order-success")
                        .param("orderCode", "LUN-123456")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("order-success"))
                .andExpect(model().attribute("orderCode", "LUN-123456"));
    }

    @Test
    @DisplayName("Customer: Trang hồ sơ và đơn hàng của tôi cho ROLE_CUSTOMER")
    void testAccountPagesForCustomer() throws Exception {
        mockMvc.perform(get("/account/profile")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("account/profile"))
                .andExpect(model().attribute("activeTab", "profile"));

        mockMvc.perform(get("/account/orders")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("account/orders"))
                .andExpect(model().attribute("activeTab", "orders"));

        mockMvc.perform(get("/account/orders/99")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(view().name("account/order-detail"))
                .andExpect(model().attribute("orderId", 99L));
    }

    @Test
    @DisplayName("Admin: Chưa đăng nhập truy cập /admin redirect sang /login")
    void testAdminUnauthenticatedRedirects() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Admin: Khách hàng truy cập /admin bị chặn 403")
    void testAdminForbiddenForCustomer() throws Exception {
        mockMvc.perform(get("/admin")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin: Nhân viên có ROLE_EMPLOYEE truy cập các trang quản trị trả về 200")
    void testAdminPagesForEmployee() throws Exception {
        mockMvc.perform(get("/admin")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attribute("activeMenu", "dashboard"));

        mockMvc.perform(get("/admin/orders")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/orders"))
                .andExpect(model().attribute("activeMenu", "orders"));

        mockMvc.perform(get("/admin/orders/10")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order-detail"))
                .andExpect(model().attribute("orderId", 10L));

        mockMvc.perform(get("/admin/inventory")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/inventory"))
                .andExpect(model().attribute("activeMenu", "inventory"));

        mockMvc.perform(get("/admin/products")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products"))
                .andExpect(model().attribute("activeMenu", "products"));
    }

    @Test
    @DisplayName("Error: Trang 403 và 404 trả về 200 và view tương ứng")
    void testErrorPages() throws Exception {
        mockMvc.perform(get("/error/403"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"));

        mockMvc.perform(get("/error/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }
}
