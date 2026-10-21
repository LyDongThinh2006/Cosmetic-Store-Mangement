package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.dto.response.review.ReviewResponse;
import com.thinh.cosmetic.service.catalog.ProductService;
import com.thinh.cosmetic.service.review.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class Phase4ProductSkuAndReviewTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Test
    @DisplayName("Admin: Thêm sản phẩm mới kèm URL ảnh đại diện chính (QLSP-QĐ6)")
    void testCreateProductWithImageUrl() throws Exception {
        String json = """
            {
                "name": "Kem Chống Nắng Quang Phổ Rộng SPF50+",
                "brandId": 1,
                "categoryId": 1,
                "origin": "Hàn Quốc",
                "description": "Chống nắng phổ rộng bảo vệ da toàn diện",
                "mainIngredients": "Zinc Oxide 12%, Titanium Dioxide",
                "uses": "Ngăn ngừa tia UVA/UVB, chống oxy hóa",
                "imageUrl": "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=600",
                "isFeatured": true,
                "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/products")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("CATALOG_MANAGE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Kem Chống Nắng Quang Phổ Rộng SPF50+"))
                .andExpect(jsonPath("$.imageUrl").value("https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=600"));
    }

    @Test
    @DisplayName("Admin: Thêm biến thể SKU mới cho sản phẩm (QLSP-QĐ5)")
    void testCreateAndManageProductSku() throws Exception {
        // 1. Tạo biến thể SKU mới cho sản phẩm #1
        String skuJson = """
            {
                "skuCode": "SKU-TEST-NEW-50ML",
                "variantName": "Dung tích 50ml Tiêu Chuẩn",
                "price": 350000,
                "listPrice": 400000,
                "barcode": "893999900001",
                "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/products/1/skus")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("CATALOG_MANAGE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skuJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.skuCode").value("SKU-TEST-NEW-50ML"))
                .andExpect(jsonPath("$.variantName").value("Dung tích 50ml Tiêu Chuẩn"))
                .andExpect(jsonPath("$.price").value(350000));

        // 2. Chặn tạo trùng mã SKU (QLSP-QĐ5)
        mockMvc.perform(post("/api/products/1/skus")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("CATALOG_MANAGE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(skuJson))
                .andExpect(status().isConflict());

        // 3. Lấy danh sách SKU của sản phẩm #1
        mockMvc.perform(get("/api/products/1/skus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Admin: Thêm Danh mục và Thương hiệu qua API")
    void testCreateCategoryAndBrand() throws Exception {
        // Thêm danh mục
        String catJson = """
            {
                "name": "Xịt Khoáng Cấp Ẩm",
                "description": "Sản phẩm làm dịu da tức thì",
                "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/categories")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("CATALOG_MANAGE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(catJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Xịt Khoáng Cấp Ẩm"));

        // Thêm thương hiệu
        String brandJson = """
            {
                "name": "Bioderma Paris",
                "description": "Thương hiệu dược mỹ phẩm hàng đầu Pháp",
                "status": "ACTIVE"
            }
            """;

        mockMvc.perform(post("/api/brands")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("CATALOG_MANAGE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brandJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bioderma Paris"));
    }

    @Test
    @DisplayName("Admin: Kiểm duyệt đánh giá - Duyệt VISIBLE và Ẩn HIDDEN (QLSP-QĐ7)")
    void testReviewModerationLifecycle() throws Exception {
        List<ReviewResponse> all = reviewService.getAll();
        assertThat(all).isNotEmpty();
        Long testReviewId = all.get(0).getId();

        // 1. Duyệt công khai (VISIBLE)
        mockMvc.perform(put("/api/reviews/" + testReviewId + "/status")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .param("status", "VISIBLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moderationStatus").value("VISIBLE"));

        // 2. Ẩn vi phạm nhưng lưu vết dữ liệu (HIDDEN per QLSP-QĐ7)
        mockMvc.perform(put("/api/reviews/" + testReviewId + "/status")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE")))
                        .header("X-Requested-With", "XMLHttpRequest")
                        .param("status", "HIDDEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moderationStatus").value("HIDDEN"));

        // 3. Lấy toàn bộ danh sách đánh giá cho admin
        mockMvc.perform(get("/api/reviews")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Admin: Trang giao diện products.html và reviews.html nạp đúng model Thymeleaf")
    void testAdminViewPages() throws Exception {
        mockMvc.perform(get("/admin/products")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/products"))
                .andExpect(model().attribute("activeMenu", "products"));

        mockMvc.perform(get("/admin/reviews")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reviews"))
                .andExpect(model().attribute("activeMenu", "reviews"));
    }
}
