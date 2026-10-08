package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CheckoutPageController {

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Thanh Toán Đơn Hàng | LUNEA");
        model.addAttribute("activeNav", "checkout");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "checkout";
    }

    @GetMapping("/order-success")
    public String orderSuccess(
            @RequestParam(name = "orderCode", required = false) String orderCode,
            @RequestParam(name = "code", required = false) String code,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        String finalOrderCode = (orderCode != null && !orderCode.isBlank()) ? orderCode : code;
        model.addAttribute("pageTitle", "Đặt Hàng Thành Công | LUNEA");
        model.addAttribute("orderCode", finalOrderCode != null ? finalOrderCode : "");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "order-success";
    }
}
