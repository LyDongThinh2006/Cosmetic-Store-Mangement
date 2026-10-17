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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class Phase1RestEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("UC08: Public API tra cứu tồn kho showroom theo SKU trả về 200")
    void testGetStoreStockBySkuEndpoint() throws Exception {
        mockMvc.perform(get("/api/products/sku/1/stores"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("UC03: Public API đặt lại mật khẩu trả về 400 nếu dữ liệu trống")
    void testResetPasswordValidation() throws Exception {
        mockMvc.perform(post("/api/auth/reset-password")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailOrPhone\":\"\", \"newPassword\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UC03: Public API gửi mã OTP trả về 400 nếu email/sđt trống")
    void testSendOtpValidation() throws Exception {
        mockMvc.perform(post("/api/auth/send-otp")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailOrPhone\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UC03: Public API gửi mã OTP trả về 200 khi tài khoản hợp lệ")
    void testSendOtpEndpoint() throws Exception {
        mockMvc.perform(post("/api/auth/send-otp")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailOrPhone\":\"khach01@lunea.test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.debugOtp").isNotEmpty());
    }

    @Test
    @DisplayName("UC01: Public API gửi mã OTP đăng ký trả về 200 cho email hợp lệ")
    void testSendRegisterOtpEndpoint() throws Exception {
        mockMvc.perform(post("/api/auth/register/send-otp")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailOrPhone\":\"new_test_customer@lunea.test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.debugOtp").isNotEmpty());
    }

    @Test
    @DisplayName("UC01: Public API gửi mã OTP đăng ký trả về 400 nếu email trống")
    void testSendRegisterOtpValidation() throws Exception {
        mockMvc.perform(post("/api/auth/register/send-otp")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"emailOrPhone\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UC14: API Wishlist trả về 200 khi khách hàng truy cập")
    void testWishlistEndpoint() throws Exception {
        mockMvc.perform(get("/api/wishlist")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("UC14: API lấy danh sách product IDs yêu thích trả về 200")
    void testWishlistProductIdsEndpoint() throws Exception {
        mockMvc.perform(get("/api/wishlist/product-ids")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("UC14: API thêm sản phẩm vào wishlist trả về 200")
    void testWishlistAddProductEndpoint() throws Exception {
        mockMvc.perform(post("/api/wishlist/products/1")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("UC14: API xóa sản phẩm khỏi wishlist trả về 200")
    void testWishlistRemoveProductEndpoint() throws Exception {
        mockMvc.perform(delete("/api/wishlist/products/1")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("UC14: API xóa toàn bộ wishlist trả về 204 No Content")
    void testWishlistClearEndpoint() throws Exception {
        mockMvc.perform(delete("/api/wishlist")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("UC26: API tra cứu yêu cầu đổi trả theo đơn hàng trả về 200")
    void testGetReturnRequestsByOrderEndpoint() throws Exception {
        mockMvc.perform(get("/api/returns/order/1")
                        .with(user("khach01").authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isOk());
    }
}
