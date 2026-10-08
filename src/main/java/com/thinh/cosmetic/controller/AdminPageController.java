package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {

    @GetMapping("/admin")
    public String dashboard(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Tổng Quan Quản Trị | LUNEA Back-office");
        model.addAttribute("activeMenu", "dashboard");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "admin/dashboard";
    }

    @GetMapping("/admin/orders")
    public String orders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Đơn Hàng Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "admin/orders";
    }

    @GetMapping("/admin/orders/{id}")
    public String orderDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        model.addAttribute("pageTitle", "Chi Tiết Đơn Hàng #" + id + " | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        model.addAttribute("orderId", id);
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "admin/order-detail";
    }

    @GetMapping("/admin/inventory")
    public String inventory(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Tồn Kho Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "inventory");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "admin/inventory";
    }

    @GetMapping("/admin/products")
    public String products(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Sản Phẩm Chuỗi | LUNEA Back-office");
        model.addAttribute("activeMenu", "products");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "admin/products";
    }
}
