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

            // Xác định chức danh thực tế của nhân sự
            String staffRoleTitle = "Nhân Viên Hệ Thống";
            if (principal.isAdmin()) {
                staffRoleTitle = "Quản Trị Viên (Admin)";
            } else if (principal.hasAnyAuthority("ROLE_WAREHOUSE_STAFF", "WAREHOUSE_STAFF")) {
                staffRoleTitle = "Quản Lý Kho / Thủ Kho";
            } else if (principal.hasAnyAuthority("ROLE_ORDER_STAFF", "ORDER_STAFF")) {
                staffRoleTitle = "Nhân Viên Đơn Hàng";
            } else if (principal.hasAnyAuthority("ROLE_PRODUCT_MANAGER", "PRODUCT_MANAGER")) {
                staffRoleTitle = "Quản Lý Sản Phẩm";
            } else if (principal.hasAnyAuthority("CUSTOMER_MANAGE", "ROLE_CUSTOMER_SERVICE")) {
                staffRoleTitle = "Chăm Sóc Khách Hàng (CSKH)";
            }
            model.addAttribute("staffRoleTitle", staffRoleTitle);

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

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/orders")
    public String orders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Đơn Hàng Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "orders");
        populateCommonAdminAttributes(principal, model);
        return "admin/orders";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN', 'ADMIN')")
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

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('INVENTORY_VIEW', 'INVENTORY_ADJUST', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/inventory")
    public String inventory(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Tồn Kho Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "inventory");
        populateCommonAdminAttributes(principal, model);
        return "admin/inventory";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/products")
    public String products(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Sản Phẩm Chuỗi | LUNEA Back-office");
        model.addAttribute("activeMenu", "products");
        populateCommonAdminAttributes(principal, model);
        return "admin/products";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('CATALOG_MANAGE', 'ROLE_PRODUCT_MANAGER', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/reviews")
    public String reviews(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Kiểm Duyệt Đánh Giá Sản Phẩm | LUNEA Back-office");
        model.addAttribute("activeMenu", "reviews");
        populateCommonAdminAttributes(principal, model);
        return "admin/reviews";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('EMPLOYEE_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/employees")
    public String employees(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Nhân Sự & Phân Quyền | LUNEA Back-office");
        model.addAttribute("activeMenu", "employees");
        populateCommonAdminAttributes(principal, model);
        return "admin/employees";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('VOUCHER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/vouchers")
    public String vouchers(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Mã Ưu Đãi & Khuyến Mãi | LUNEA Back-office");
        model.addAttribute("activeMenu", "vouchers");
        populateCommonAdminAttributes(principal, model);
        return "admin/vouchers";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/suppliers")
    public String suppliers(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Nhà Cung Cấp | LUNEA Back-office");
        model.addAttribute("activeMenu", "suppliers");
        populateCommonAdminAttributes(principal, model);
        return "admin/suppliers";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('PURCHASE_MANAGE', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/purchase-orders")
    public String purchaseOrders(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Nhập Hàng (PO) | LUNEA Back-office");
        model.addAttribute("activeMenu", "purchase-orders");
        populateCommonAdminAttributes(principal, model);
        return "admin/purchase-orders";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('INVENTORY_VIEW', 'INVENTORY_ADJUST', 'ROLE_WAREHOUSE_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/stock-transfers")
    public String stockTransfers(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Điều Chuyển Kho Liên Chi Nhánh | LUNEA Back-office");
        model.addAttribute("activeMenu", "stock-transfers");
        populateCommonAdminAttributes(principal, model);
        return "admin/stock-transfers";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('CUSTOMER_MANAGE', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/customers")
    public String customers(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Quản Lý Khách Hàng & Tài Khoản | LUNEA Back-office");
        model.addAttribute("activeMenu", "customers");
        populateCommonAdminAttributes(principal, model);
        return "admin/customers";
    }

    @PreAuthorize("authentication.name == 'admin' or hasAnyAuthority('ORDER_VIEW', 'ORDER_PROCESS', 'CUSTOMER_MANAGE', 'ROLE_ORDER_STAFF', 'ROLE_ADMIN', 'ADMIN')")
    @GetMapping("/admin/returns")
    public String returns(@AuthenticationPrincipal AuthPrincipal principal, Model model) {
        model.addAttribute("pageTitle", "Tiếp Nhận & Thẩm Định Đổi Trả Hàng | LUNEA Back-office");
        model.addAttribute("activeMenu", "returns");
        populateCommonAdminAttributes(principal, model);
        return "admin/returns";
    }
}
