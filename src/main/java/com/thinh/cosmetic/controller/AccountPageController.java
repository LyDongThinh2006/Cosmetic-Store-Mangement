package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.thinh.cosmetic.service.account.CustomerService;
import com.thinh.cosmetic.service.order.OrderService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountPageController {

    private final CustomerService customerService;
    private final OrderService orderService;

    @GetMapping("/account")
    public String account() {
        return "redirect:/account/profile";
    }

    @GetMapping("/account/profile")
    public String profile(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Thông Tin Cá Nhân & Sổ Địa Chỉ | LUNEA");
        model.addAttribute("activeNav", "account");
        model.addAttribute("activeTab", "profile");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
            Long customerId = principal.getCustomerId() != null ? principal.getCustomerId() : 1L;
            try {
                model.addAttribute("customer", customerService.getProfile(customerId));
                model.addAttribute("addresses", customerService.getAddresses(customerId));
            } catch (Exception e) {
                log.warn("Failed to load customer profile for ID {}: {}", customerId, e.getMessage());
            }
        }
        return "account/profile";
    }

    @GetMapping("/account/orders")
    public String orders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Đơn Hàng Của Tôi | LUNEA");
        model.addAttribute("activeNav", "account");
        model.addAttribute("activeTab", "orders");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
            Long customerId = principal.getCustomerId() != null ? principal.getCustomerId() : 1L;
            try {
                model.addAttribute("orders", orderService.getByCustomer(customerId));
            } catch (Exception e) {
                log.warn("Failed to load orders for customer ID {}: {}", customerId, e.getMessage());
            }
        }
        return "account/orders";
    }

    @GetMapping("/account/orders/{id}")
    public String orderDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        model.addAttribute("pageTitle", "Chi Tiết Đơn Hàng #" + id + " | LUNEA");
        model.addAttribute("activeNav", "account");
        model.addAttribute("activeTab", "orders");
        model.addAttribute("orderId", id);
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        try {
            model.addAttribute("order", orderService.getById(id));
        } catch (Exception e) {
            log.warn("Failed to load order with ID {}: {}", id, e.getMessage());
        }
        return "account/order-detail";
    }
}
