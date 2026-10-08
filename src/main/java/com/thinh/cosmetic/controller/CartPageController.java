package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.thinh.cosmetic.service.cart.CartService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartPageController {

    private final CartService cartService;

    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Giỏ Hàng Của Bạn | LUNEA");
        model.addAttribute("activeNav", "cart");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
            Long customerId = principal.getCustomerId() != null ? principal.getCustomerId() : 1L;
            try {
                model.addAttribute("cart", cartService.getCart(customerId));
            } catch (Exception e) {
                log.warn("Failed to load cart for customer {}: {}", customerId, e.getMessage());
            }
        }
        return "cart";
    }
}
