package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.security.AuthPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.transaction.annotation.Transactional
public class Phase3CustomerAndReturnTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private static RequestPostProcessor authPrincipal(Long customerId, Long employeeId, AccountType type, String... authorities) {
        List<SimpleGrantedAuthority> authList = Arrays.stream(authorities)
                .map(SimpleGrantedAuthority::new)
                .toList();

        AuthPrincipal principal = AuthPrincipal.builder()
                .accountId(100L)
                .customerId(customerId)
                .employeeId(employeeId)
                .displayName("Staff CSKH")
                .username("cskh_staff")
                .accountType(type)
                .authorities(authList)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "n/a", authList);
        return org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication(auth);
    }

    // ─── 1. Web Page Routes (/admin/customers, /admin/returns) ───

    @Test
    @DisplayName("Route /admin/customers: Quản trị viên/CSKH có CUSTOMER_MANAGE truy cập thành công (200 OK)")
    void adminCustomersPage_accessibleByCustomerManage() throws Exception {
        mockMvc.perform(get("/admin/customers")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ADMIN", "ADMIN", "CUSTOMER_MANAGE")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Route /admin/returns: Nhân viên Đơn hàng/CSKH có ORDER_VIEW/ORDER_PROCESS truy cập thành công (200 OK)")
    void adminReturnsPage_accessibleByOrderStaff() throws Exception {
        mockMvc.perform(get("/admin/returns")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ORDER_STAFF", "ORDER_VIEW", "ORDER_PROCESS")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Route /admin/customers: Chưa đăng nhập bị chuyển hướng (302)")
    void adminCustomersPage_anonymous_shouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/customers"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Route /admin/returns: Chưa đăng nhập bị chuyển hướng (302)")
    void adminReturnsPage_anonymous_shouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/returns"))
                .andExpect(status().is3xxRedirection());
    }

    // ─── 2. Customer Rest APIs (Lock/Unlock, Orders history) ───

    @Test
    @DisplayName("API /api/customers: CSKH xem danh sách khách hàng thành công (200 OK)")
    void getAllCustomersApi_success() throws Exception {
        mockMvc.perform(get("/api/customers")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE", "CUSTOMER_MANAGE")))
                .andExpect(status().isOk());
    }

    @Autowired
    private com.thinh.cosmetic.repository.account.AccountRepository accountRepository;

    @Test
    @DisplayName("API /api/customers/{id}/lock & /unlock: Khóa và mở khóa tài khoản khách hàng thành công (QLKH-QĐ2)")
    void lockAndUnlockCustomer_success() throws Exception {
        CustomerEntity customer = customerRepository.findAll().stream().findFirst().orElse(null);
        if (customer != null) {
            Long custId = customer.getId();
            Long accId = customer.getAccount().getId();

            // 1. Lock customer
            mockMvc.perform(put("/api/customers/" + custId + "/lock")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ADMIN", "ADMIN", "CUSTOMER_MANAGE")))
                    .andExpect(status().isOk())
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status").value("LOCKED"));

            assertEquals(ActiveStatus.LOCKED, accountRepository.findById(accId).orElseThrow().getStatus());

            // 2. Unlock customer
            mockMvc.perform(put("/api/customers/" + custId + "/unlock")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ADMIN", "ADMIN", "CUSTOMER_MANAGE")))
                    .andExpect(status().isOk())
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE"));

            assertEquals(ActiveStatus.ACTIVE, accountRepository.findById(accId).orElseThrow().getStatus());
        }
    }

    @Test
    @DisplayName("API /api/customers/{id}/status: Cập nhật trạng thái LOCKED / ACTIVE qua query param")
    void updateCustomerStatusParam_success() throws Exception {
        CustomerEntity customer = customerRepository.findAll().stream().findFirst().orElse(null);
        if (customer != null) {
            Long custId = customer.getId();
            Long accId = customer.getAccount().getId();

            // Set to LOCKED
            mockMvc.perform(put("/api/customers/" + custId + "/status?status=LOCKED")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ADMIN", "ADMIN", "CUSTOMER_MANAGE")))
                    .andExpect(status().isOk())
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status").value("LOCKED"));

            assertEquals(ActiveStatus.LOCKED, accountRepository.findById(accId).orElseThrow().getStatus());

            // Set back to ACTIVE
            mockMvc.perform(put("/api/customers/" + custId + "/status?status=ACTIVE")
                            .header("X-Requested-With", "XMLHttpRequest")
                            .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_ADMIN", "ADMIN", "CUSTOMER_MANAGE")))
                    .andExpect(status().isOk())
                    .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE"));

            assertEquals(ActiveStatus.ACTIVE, accountRepository.findById(accId).orElseThrow().getStatus());
        }
    }

    @Test
    @DisplayName("API /api/customers/{id}/orders: Tra cứu lịch sử đơn hàng của khách hàng (200 OK)")
    void getCustomerOrdersApi_success() throws Exception {
        CustomerEntity customer = customerRepository.findAll().stream().findFirst().orElse(null);
        if (customer != null) {
            mockMvc.perform(get("/api/customers/" + customer.getId() + "/orders")
                            .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE", "CUSTOMER_MANAGE")))
                    .andExpect(status().isOk());
        }
    }

    // ─── 3. Return Rest APIs ───

    @Test
    @DisplayName("API /api/returns: Nhân viên xem danh sách yêu cầu đổi trả (200 OK)")
    void getAllReturnsApi_success() throws Exception {
        mockMvc.perform(get("/api/returns")
                        .with(authPrincipal(null, 1L, AccountType.EMPLOYEE, "ROLE_EMPLOYEE", "ORDER_PROCESS")))
                .andExpect(status().isOk());
    }
}
