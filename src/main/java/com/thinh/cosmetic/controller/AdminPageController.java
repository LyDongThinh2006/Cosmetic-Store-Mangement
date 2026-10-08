package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.dto.response.store.StoreResponse;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.security.StoreScope;
import com.thinh.cosmetic.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        model.addAttribute("pageTitle", "Tổng Quan Quản Trị | LUNEA Back-office");
        model.addAttribute("activeMenu", "dashboard");
        populateCommonAdminAttributes(principal, model);
        return "admin/dashboard";
    }

    @GetMapping("/admin/orders")
    public String orders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Đơn Hàng Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        populateCommonAdminAttributes(principal, model);
        return "admin/orders";
    }

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

    @GetMapping("/admin/inventory")
    public String inventory(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Tồn Kho Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "inventory");
        populateCommonAdminAttributes(principal, model);
        return "admin/inventory";
    }

    @GetMapping("/admin/products")
    public String products(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Sản Phẩm Chuỗi | LUNEA Back-office");
        model.addAttribute("activeMenu", "products");
        populateCommonAdminAttributes(principal, model);
        return "admin/products";
    }
}
