package com.thinh.cosmetic.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Step 16: Khách hàng gọi API Admin /api/admin/orders nhận 403 Forbidden")
    void testCustomerCannotAccessAdminOrdersApi() throws Exception {
        mockMvc.perform(get("/api/admin/orders")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin / Nhân viên gọi API Admin /api/admin/orders nhận 200 OK")
    void testEmployeeCanAccessAdminOrdersApi() throws Exception {
        mockMvc.perform(get("/api/admin/orders")
                        .with(user("order_hcm").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Khách hàng truy cập /api/orders/my-orders nhận 200 OK")
    void testCustomerCanAccessMyOrdersApi() throws Exception {
        mockMvc.perform(get("/api/orders/my-orders")
                        .param("customerId", "1")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    @DisplayName("Chưa đăng nhập gọi /api/orders/preview bị chặn 401 Unauthorized khi gửi kèm X-Requested-With")
    void testUnauthenticatedCannotAccessOrderPreview() throws Exception {
        mockMvc.perform(post("/api/orders/preview")
                        .header("X-Requested-With", "Fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"voucherCode\":\"LUNEA10\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Trang thanh toán /checkout yêu cầu đăng nhập và chuyển hướng /login nếu chưa xác thực")
    void testCheckoutPageAccess() throws Exception {
        // Chưa đăng nhập -> 302 Redirect về trang login
        mockMvc.perform(get("/checkout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?redirect=%2Fcheckout"));

        // Đã đăng nhập vai trò khách hàng -> 200
        mockMvc.perform(get("/checkout")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Trang quản trị /admin/orders chỉ cho phép ROLE_EMPLOYEE")
    void testAdminOrdersPageAccess() throws Exception {
        // Khách hàng truy cập /admin/orders -> 403
        mockMvc.perform(get("/admin/orders")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());

        // Nhân viên truy cập /admin/orders -> 200
        mockMvc.perform(get("/admin/orders")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk());
    }
}
