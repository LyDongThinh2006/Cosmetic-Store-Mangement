package com.thinh.cosmetic.controller;

import com.thinh.cosmetic.domain.dto.response.cart.CartItemResponse;
import com.thinh.cosmetic.domain.dto.response.cart.CartResponse;
import com.thinh.cosmetic.domain.enums.AccountType;
import com.thinh.cosmetic.exception.GlobalExceptionHandler;
import com.thinh.cosmetic.rest.cart.CartRestController;
import com.thinh.cosmetic.security.AuthPrincipal;
import com.thinh.cosmetic.service.cart.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CartRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartRestController cartRestController;

    @BeforeEach
    void setUp() {
        AuthPrincipal dummyPrincipal = AuthPrincipal.builder()
                .accountId(1L)
                .customerId(100L)
                .displayName("Customer Test")
                .username("khach01")
                .accountType(AccountType.CUSTOMER)
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")))
                .build();

        HandlerMethodArgumentResolver principalResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(AuthPrincipal.class);
            }

            @Override
            public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                return dummyPrincipal;
            }
        };

        mockMvc = MockMvcBuilders.standaloneSetup(cartRestController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(principalResolver)
                .build();
    }

    private CartResponse mockCartResponse(Long itemId, int quantity) {
        CartItemResponse item = CartItemResponse.builder()
                .id(itemId)
                .skuId(10L)
                .productName("Test Serum")
                .quantity(quantity)
                .price(new BigDecimal("100000"))
                .subtotal(new BigDecimal(100000L * quantity))
                .build();
        return CartResponse.builder()
                .id(1L)
                .items(List.of(item))
                .totalItems(quantity)
                .subtotal(new BigDecimal(100000L * quantity))
                .build();
    }

    @Test
    @DisplayName("PUT /api/cart/items/{id} với JSON body {quantity: 3} thành công trả về 200")
    void testUpdateCartItemWithJsonBody_PUT() throws Exception {
        when(cartService.updateItemQuantity(anyLong(), anyLong(), anyInt()))
                .thenReturn(mockCartResponse(5L, 3));

        mockMvc.perform(put("/api/cart/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(3))
                .andExpect(jsonPath("$.subtotal").value(300000))
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }

    @Test
    @DisplayName("PATCH /api/cart/items/{id} với JSON body {quantity: 4} thành công trả về 200")
    void testUpdateCartItemWithJsonBody_PATCH() throws Exception {
        when(cartService.updateItemQuantity(anyLong(), anyLong(), anyInt()))
                .thenReturn(mockCartResponse(5L, 4));

        mockMvc.perform(patch("/api/cart/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(4));
    }

    @Test
    @DisplayName("PUT /api/cart/items/{id}?quantity=2 với query parameter fallback thành công trả về 200")
    void testUpdateCartItemWithQueryParam() throws Exception {
        when(cartService.updateItemQuantity(anyLong(), anyLong(), anyInt()))
                .thenReturn(mockCartResponse(5L, 2));

        mockMvc.perform(put("/api/cart/items/5?quantity=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2));
    }

    @Test
    @DisplayName("PUT /api/cart/items/{id} không truyền quantity trả về 400 Bad Request")
    void testUpdateCartItemMissingQuantity() throws Exception {
        mockMvc.perform(put("/api/cart/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/cart/items/{id} với quantity < 1 trả về 400 Bad Request")
    void testUpdateCartItemInvalidQuantity_TooLow() throws Exception {
        mockMvc.perform(put("/api/cart/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/cart/items/{id} với quantity > 99 trả về 400 Bad Request")
    void testUpdateCartItemInvalidQuantity_TooHigh() throws Exception {
        mockMvc.perform(put("/api/cart/items/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\": 100}"))
                .andExpect(status().isBadRequest());
    }
}
