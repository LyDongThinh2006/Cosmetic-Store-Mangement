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
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "LUNEA | Thế Giới Mỹ Phẩm & Chăm Sóc Sắc Đẹp");
        model.addAttribute("activeNav", "home");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "home";
    }

    @GetMapping("/stores")
    public String stores(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Hệ Thống Chuỗi Cửa Hàng | LUNEA");
        model.addAttribute("activeNav", "stores");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "stores";
    }

    @GetMapping("/brands")
    public String brands(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Thương Hiệu Đồng Hành | LUNEA");
        model.addAttribute("activeNav", "brands");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "brands";
    }
}
