package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.response.cart.WishListResponse;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.domain.entity.catalog.WishListEntity;
import com.thinh.cosmetic.domain.entity.catalog.WishListItemEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductImageEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.repository.cart.WishListItemRepository;
import com.thinh.cosmetic.repository.cart.WishListRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.repository.catalog.ProductSkuRepository;
import com.thinh.cosmetic.service.cart.CartService;
import com.thinh.cosmetic.service.cart.impl.WishListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private WishListItemRepository wishListItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ProductSkuRepository productSkuRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private WishListServiceImpl wishListService;

    private CustomerEntity customer;
    private WishListEntity wishList;
    private ProductEntity product;
    private WishListItemEntity wishListItem;
    private ProductSkuEntity sku;

    @BeforeEach
    void setUp() {
        customer = CustomerEntity.builder().id(1L).fullName("Minh Anh").build();
        wishList = WishListEntity.builder().id(10L).customer(customer).build();

        sku = ProductSkuEntity.builder()
                .id(100L)
                .price(BigDecimal.valueOf(350000))
                .status(ActiveStatus.ACTIVE)
                .build();
        ProductImageEntity img = ProductImageEntity.builder().id(1L).imageUrl("https://example.com/serum.jpg").build();

        product = ProductEntity.builder()
                .id(5L)
                .name("Serum B5")
                .build();

        wishListItem = WishListItemEntity.builder()
                .id(20L)
                .wishList(wishList)
                .product(product)
                .build();
    }

    @Test
    @DisplayName("UC14: Get wishlist returns items mapped to response")
    void testGetWishList() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));
        when(wishListItemRepository.findByWishListId(10L)).thenReturn(List.of(wishListItem));
        when(productSkuRepository.findByProductId(5L)).thenReturn(List.of(sku));

        WishListResponse response = wishListService.getWishList(1L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(1, response.getItems().size());
        assertEquals(5L, response.getItems().get(0).getProductId());
        assertEquals("Serum B5", response.getItems().get(0).getProductName());
        assertEquals(BigDecimal.valueOf(350000), response.getItems().get(0).getPrice());
    }

    @Test
    @DisplayName("UC14: Add product to wishlist saves new item")
    void testAddProduct() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(wishListItemRepository.findByWishListIdAndProductId(10L, 5L)).thenReturn(Optional.empty());
        when(wishListItemRepository.findByWishListId(10L)).thenReturn(List.of(wishListItem));
        when(productSkuRepository.findByProductId(5L)).thenReturn(List.of(sku));

        WishListResponse response = wishListService.addProduct(1L, 5L);

        verify(wishListItemRepository).save(any(WishListItemEntity.class));
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
    }

    @Test
    @DisplayName("UC14: Remove product from wishlist deletes by productId")
    void testRemoveProduct() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));
        when(wishListItemRepository.findByWishListId(10L)).thenReturn(Collections.emptyList());

        WishListResponse response = wishListService.removeProduct(1L, 5L);

        verify(wishListItemRepository).deleteByWishListIdAndProductId(10L, 5L);
        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
    }

    @Test
    @DisplayName("UC14: Clear wishlist deletes all items by wishListId")
    void testClearWishList() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));

        wishListService.clearWishList(1L);

        verify(wishListItemRepository).deleteByWishListId(10L);
    }

    @Test
    @DisplayName("UC14: Get wishlist product IDs returns list of IDs")
    void testGetWishListProductIds() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));
        when(wishListItemRepository.findByWishListId(10L)).thenReturn(List.of(wishListItem));

        List<Long> ids = wishListService.getWishListProductIds(1L);

        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertEquals(5L, ids.get(0));
    }

    @Test
    @DisplayName("UC14: Move all to cart adds items to cart")
    void testMoveAllToCart() throws Exception {
        when(wishListRepository.findByCustomerId(1L)).thenReturn(Optional.of(wishList));
        when(wishListItemRepository.findByWishListId(10L)).thenReturn(List.of(wishListItem));
        when(productSkuRepository.findByProductId(5L)).thenReturn(List.of(sku));

        WishListResponse response = wishListService.moveAllToCart(1L);

        verify(cartService).addItem(eq(1L), any());
        assertNotNull(response);
    }
}
