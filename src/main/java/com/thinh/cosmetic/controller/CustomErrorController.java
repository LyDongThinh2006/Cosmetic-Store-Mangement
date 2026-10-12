package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, @AuthenticationPrincipal AuthPrincipal principal, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (status != null) {
            try {
                statusCode = Integer.parseInt(status.toString());
            } catch (NumberFormatException ignored) {}
        }

        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute("pageTitle", "404 - Không Tìm Thấy Trang | LUNEA");
            return "error/404";
        }
        if (statusCode == HttpStatus.FORBIDDEN.value()) {
            model.addAttribute("pageTitle", "403 - Quyền Truy Cập Bị Từ Chối | LUNEA");
            return "error/403";
        }

        model.addAttribute("pageTitle", "500 - Lỗi Máy Chủ | LUNEA");
        model.addAttribute("statusCode", statusCode);
        return "error/500";
    }

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

    @GetMapping("/error/500")
    public String serverError(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "500 - Lỗi Máy Chủ | LUNEA");
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "error/500";
    }
}
