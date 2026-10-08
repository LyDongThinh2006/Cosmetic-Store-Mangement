package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CartPageController {

    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Giỏ Hàng Của Bạn | LUNEA");
        model.addAttribute("activeNav", "cart");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "cart";
    }
}
