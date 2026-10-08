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
public class AuthPageController {

    @GetMapping("/login")
    public String login(
            @RequestParam(name = "redirect", required = false, defaultValue = "/") String redirect,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        if (principal != null) {
            if (principal.isEmployee()) {
                return "redirect:/admin";
            }
            return "redirect:" + (redirect.startsWith("/") ? redirect : "/");
        }
        model.addAttribute("pageTitle", "Đăng Nhập Tài Khoản | LUNEA");
        model.addAttribute("redirect", redirect);
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        if (principal != null) {
            if (principal.isEmployee()) {
                return "redirect:/admin";
            }
            return "redirect:/";
        }
        model.addAttribute("pageTitle", "Đăng Ký Tài Khoản | LUNEA");
        return "auth/register";
    }
}
