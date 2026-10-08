package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinh.cosmetic.service.account.CustomerService;
import com.thinh.cosmetic.service.cart.CartService;
import com.thinh.cosmetic.service.order.OrderService;
import com.thinh.cosmetic.service.store.StoreService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CheckoutPageController {

    private final CustomerService customerService;
    private final CartService cartService;
    private final StoreService storeService;
    private final OrderService orderService;

    @GetMapping("/checkout")
    public String checkout(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Thanh Toán Đơn Hàng | LUNEA");
        model.addAttribute("activeNav", "checkout");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
            Long customerId = principal.getCustomerId() != null ? principal.getCustomerId() : 1L;
            try {
                model.addAttribute("addresses", customerService.getAddresses(customerId));
                model.addAttribute("cart", cartService.getCart(customerId));
            } catch (Exception e) {
                log.warn("Failed to load checkout data: {}", e.getMessage());
            }
        }
        model.addAttribute("stores", storeService.getAll());
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
        if (finalOrderCode != null && !finalOrderCode.isBlank()) {
            try {
                model.addAttribute("order", orderService.getByOrderCode(finalOrderCode));
            } catch (Exception e) {
                log.warn("Failed to load order for code {}: {}", finalOrderCode, e.getMessage());
            }
        }
        return "order-success";
    }
}
