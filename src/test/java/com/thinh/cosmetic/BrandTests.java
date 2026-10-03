package com.thinh.cosmetic;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.service.catalog.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@Transactional
public class BrandTests {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BrandService brandService;

    @Autowired
    public BrandTests(MockMvc mockMvc, BrandService brandService) {
        this.mockMvc = mockMvc;
        this.brandService = brandService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatReturn201WhenPostBrand() throws Exception {
        BrandRequest request = BrandRequest.builder()
                .name("Perfume")
                .description("This perfume is good")
                .status(ActiveStatus.ACTIVE)
                .build();

        String brandJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/brands")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(brandJson)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isCreated()
                );
    }

    @Test
    public void testThatReturnBrandWhenPostBrand() throws Exception {
        BrandRequest request = BrandRequest.builder()
                .name("Perfume")
                .description("This perfume is good")
                .status(ActiveStatus.ACTIVE)
                .build();

        String brandJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/brands")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(brandJson)
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.id").isNumber()
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.name").value("Perfume")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE")
        );
    }

    @Test
    public void testThatReturn200WhenGetBrand() throws Exception {
        brandService.create(
                BrandRequest.builder()
                        .name("Perfume")
                        .description("This perfume is good")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/brands/" + 1)
                ).andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    public void testThatReturnBrandWhenGetBrand() throws Exception {
        brandService.create(
                BrandRequest.builder()
                        .name("Perfume")
                        .description("This perfume is good")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        brandService.create(
                BrandRequest.builder()
                        .name("Skincare")
                        .description("Skincare brand")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/brands")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$").isArray()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Perfume")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].name").value("Skincare")
        );
    }

    @Test
    public void testThatReturn200WhenPutBrand() throws Exception {
        brandService.create(
                BrandRequest.builder()
                        .name("Perfume")
                        .description("This perfume is good")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        BrandRequest request = BrandRequest.builder()
                .name("Skincare")
                .description("Skincare brand")
                .status(ActiveStatus.INACTIVE)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatReturnBrandWhenPutBrand() throws Exception {
        brandService.create(
                BrandRequest.builder()
                        .name("Perfume")
                        .description("This perfume is good")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        BrandRequest request = BrandRequest.builder()
                .name("Skincare")
                .description("Skincare brand")
                .status(ActiveStatus.INACTIVE)
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Skincare")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("Skincare brand")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value("INACTIVE")
        );
    }

    @Test
    public void testThatReturn204WhenDeleteBrand() throws Exception {
        brandService.create(
                BrandRequest.builder()
                        .name("Perfume")
                        .description("This perfume is good")
                        .status(ActiveStatus.ACTIVE)
                        .build()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/brands/1")
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
