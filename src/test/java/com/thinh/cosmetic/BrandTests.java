package com.thinh.cosmetic;

import com.thinh.cosmetic.domain.dto.request.catalog.BrandRequest;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.service.catalog.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BrandTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BrandService brandService;

    @Autowired
    public BrandTests(MockMvc mockMvc, BrandService brandService) {
        this.mockMvc = mockMvc;
        this.brandService = brandService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatReturn201WhenCreateBrand() throws Exception {
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
    public void testThatReturnBrandWhenCreateBrand() throws Exception {
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
                        MockMvcResultMatchers.jsonPath("$.description").value("This perfume is good")
                ).andExpect(
                        MockMvcResultMatchers.jsonPath("$.status").value("ACTIVE")
                );
    }
}
