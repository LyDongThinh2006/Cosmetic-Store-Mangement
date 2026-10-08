package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CustomErrorController {

    @GetMapping("/error/403")
    public String accessDenied(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "403 - Quyền Truy Cập Bị Từ Chối | LUNEA");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "error/403";
    }

    @GetMapping("/error/404")
    public String notFound(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "404 - Không Tìm Thấy Trang | LUNEA");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "error/404";
    }
}
