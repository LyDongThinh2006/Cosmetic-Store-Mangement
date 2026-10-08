package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.security.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinh.cosmetic.domain.dto.response.catalog.ProductResponse;
import com.thinh.cosmetic.service.catalog.BrandService;
import com.thinh.cosmetic.service.catalog.CategoryService;
import com.thinh.cosmetic.service.catalog.ProductService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public String productList(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "brandId", required = false) Long brandId,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "skinType", required = false) String skinType,
            @RequestParam(name = "concern", required = false) String concern,
            @RequestParam(name = "priceRange", required = false) String priceRange,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        model.addAttribute("pageTitle", "Danh Sách Sản Phẩm | LUNEA");
        model.addAttribute("activeNav", "products");
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("brandId", brandId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("skinType", skinType);
        model.addAttribute("concern", concern);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("sortBy", sortBy != null ? sortBy : "newest");
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("products", productService.getAll());
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal AuthPrincipal principal,
            Model model
    ) {
        ProductResponse product = null;
        try {
            product = productService.getById(id);
        } catch (Exception e) {
            log.warn("Product not found with id: {}", id);
        }
        model.addAttribute("pageTitle", product != null ? product.getName() + " | LUNEA" : "Chi Tiết Sản Phẩm | LUNEA");
        model.addAttribute("activeNav", "products");
        model.addAttribute("productId", id);
        model.addAttribute("product", product);
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "product-detail";
    }
}
