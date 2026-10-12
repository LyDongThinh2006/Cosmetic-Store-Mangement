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

import java.util.List;
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

    @GetMapping({"/products", "/product"})
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
        model.addAttribute("keyword", keyword != null ? keyword.trim() : "");
        model.addAttribute("brandId", brandId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("skinType", skinType);
        model.addAttribute("concern", concern);
        model.addAttribute("priceRange", priceRange);
        model.addAttribute("sortBy", sortBy != null ? sortBy : "newest");
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("categories", categoryService.getAll());

        List<ProductResponse> products = productService.searchAndFilter(
                keyword, brandId, categoryId, skinType, concern, priceRange, sortBy
        );
        model.addAttribute("products", products);

        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "product-list";
    }

    @GetMapping({"/products/{id}", "/product/{id}"})
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

        if (product == null) {
            return "redirect:/error/404";
        }

        final Long catId = product.getCategory() != null ? product.getCategory().getId() : null;
        final Long brandId = product.getBrand() != null ? product.getBrand().getId() : null;

        // Suggested / Related Products in same category or brand
        List<ProductResponse> relatedProducts = productService.getAll().stream()
                .filter(p -> !id.equals(p.getId()))
                .filter(p -> (catId != null && p.getCategory() != null && catId.equals(p.getCategory().getId()))
                        || (brandId != null && p.getBrand() != null && brandId.equals(p.getBrand().getId())))
                .limit(4)
                .toList();
        model.addAttribute("relatedProducts", relatedProducts);

        model.addAttribute("pageTitle", product.getName() + " | LUNEA");
        model.addAttribute("activeNav", "products");
        model.addAttribute("productId", id);
        model.addAttribute("product", product);
        if (principal != null) {
            model.addAttribute("currentUser", principal);
        }
        return "product-detail";
    }

    private boolean idMatch(Long id1, Long id2) {
        return id1 != null && id1.equals(id2);
    }
}
