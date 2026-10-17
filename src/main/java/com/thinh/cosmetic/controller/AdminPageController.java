package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.security.StoreScope;
import com.thinh.cosmetic.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {

    private final StoreService storeService;
    private final StoreScope storeScope;

    private void populateCommonAdminAttributes(AuthPrincipal principal, Model model) {
        List<StoreResponse> allStores = storeService.getAll();
        model.addAttribute("allStores", allStores);

        if (principal != null) {
            model.addAttribute("currentUser", principal);
            List<Long> accessibleIds = storeScope.getAccessibleStoreIds(principal);
            if (accessibleIds == null) {
                model.addAttribute("isAllStoresAdmin", true);
                model.addAttribute("accessibleStores", allStores);
                model.addAttribute("primaryStoreName", "Toàn hệ thống (Admin)");
                model.addAttribute("primaryStoreId", null);
            } else {
                model.addAttribute("isAllStoresAdmin", false);
                List<StoreResponse> staffStores = allStores.stream()
                        .filter(s -> accessibleIds.contains(s.getId()))
                        .toList();
                model.addAttribute("accessibleStores", staffStores);
                if (!staffStores.isEmpty()) {
                    model.addAttribute("primaryStoreName", staffStores.get(0).getName());
                    model.addAttribute("primaryStoreId", staffStores.get(0).getId());
                } else {
                    model.addAttribute("primaryStoreName", "Chưa gán chi nhánh");
                    model.addAttribute("primaryStoreId", null);
                }
            }
        }
    }

    @GetMapping("/admin")
    public String dashboard(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        if (principal != null) {
            boolean canViewReports = principal.isAdmin() || principal.hasAuthority("REPORT_VIEW");
            if (!canViewReports) {
                // Điều hướng nhân viên chuyên trách đến đúng nghiệp vụ của họ
                if (principal.hasAnyAuthority("ROLE_WAREHOUSE_STAFF", "INVENTORY_ADJUST")
                        && !principal.hasAnyAuthority("ROLE_ORDER_STAFF", "ORDER_VIEW")) {
                    return "redirect:/admin/inventory";
                }
                if (principal.hasAnyAuthority("ROLE_ORDER_STAFF", "ORDER_VIEW")) {
                    return "redirect:/admin/orders";
                }
                if (principal.hasAnyAuthority("ROLE_PRODUCT_MANAGER", "CATALOG_MANAGE")) {
                    return "redirect:/admin/products";
                }
                if (principal.hasAnyAuthority("EMPLOYEE_MANAGE")) {
                    return "redirect:/admin/employees";
                }
            }
        }

        model.addAttribute("pageTitle", "Tổng Quan Quản Trị | LUNEA Back-office");
        model.addAttribute("activeMenu", "dashboard");
        populateCommonAdminAttributes(principal, model);
        return "admin/dashboard";
    }

    @PreAuthorize("hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN')")
    @GetMapping("/admin/orders")
    public String orders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Đơn Hàng Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        populateCommonAdminAttributes(principal, model);
        return "admin/orders";
    }

    @PreAuthorize("hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN')")
    @GetMapping("/admin/orders/{id}")
    public String orderDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        model.addAttribute("pageTitle", "Chi Tiết Đơn Hàng #" + id + " | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        model.addAttribute("orderId", id);
        populateCommonAdminAttributes(principal, model);
        return "admin/order-detail";
    }

    @PreAuthorize("hasAnyAuthority('INVENTORY_VIEW', 'INVENTORY_ADJUST', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN')")
    @GetMapping("/admin/inventory")
    public String inventory(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Tồn Kho Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "inventory");
        populateCommonAdminAttributes(principal, model);
        return "admin/inventory";
    }

    @PreAuthorize("hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/admin/products")
    public String products(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Sản Phẩm Chuỗi | LUNEA Back-office");
        model.addAttribute("activeMenu", "products");
        populateCommonAdminAttributes(principal, model);
        return "admin/products";
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYEE_MANAGE', 'ROLE_ADMIN')")
    @GetMapping("/admin/employees")
    public String employees(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Nhân Sự & Phân Quyền | LUNEA Back-office");
        model.addAttribute("activeMenu", "employees");
        populateCommonAdminAttributes(principal, model);
        return "admin/employees";
    }
}
