package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthCookieFactory;
import com.thinh.cosmetic.security.AuthPrincipal;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthCookieFactory authCookieFactory;

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        ResponseCookie logoutCookie = authCookieFactory.createLogoutCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, logoutCookie.toString());

        Cookie cookie = new Cookie(AuthCookieFactory.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

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
