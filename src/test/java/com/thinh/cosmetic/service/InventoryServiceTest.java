package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.store.InventoryAdjustmentRequest;
import com.thinh.cosmetic.domain.dto.response.store.InventoryResponse;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.store.InventoryAdjustmentEntity;
import com.thinh.cosmetic.domain.entity.store.InventoryEntity;
import com.thinh.cosmetic.domain.entity.store.StoreEntity;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.store.InventoryAdjustmentRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.service.store.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryAdjustmentRepository adjustmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private StoreEntity store;
    private ProductSkuEntity sku;
    private InventoryEntity inventory;

    @BeforeEach
    void setUp() {
        store = StoreEntity.builder().id(1L).name("Lunea Flagship Store").build();
        ProductEntity product = ProductEntity.builder().id(1L).name("Lipstick Matte").build();
        sku = ProductSkuEntity.builder().id(10L).skuCode("SKU-LIP-01").product(product).build();

        inventory = InventoryEntity.builder()
                .id(100L)
                .store(store)
                .sku(sku)
                .actualStock(50)
                .heldQuantity(10)
                .minimumStock(5)
                .build();
    }

    @Test
    @DisplayName("Calculate available stock: actualStock - heldQuantity")
    void testGetAvailableStock() {
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 10L)).thenReturn(Optional.of(inventory));

        Integer available = inventoryService.getAvailableStock(1L, 10L);
        assertEquals(40, available);
    }

    @Test
    @DisplayName("Adjust inventory stock creates adjustment history and updates actual stock")
    void testAdjustStock() throws Exception {
        when(inventoryRepository.findByStoreIdAndSkuId(1L, 10L)).thenReturn(Optional.of(inventory));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(EmployeeEntity.builder().id(1L).build()));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenAnswer(i -> i.getArgument(0));

        InventoryAdjustmentRequest request = InventoryAdjustmentRequest.builder()
                .storeId(1L)
                .skuId(10L)
                .newQuantity(65)
                .reason("Damaged stock recount and add")
                .build();

        InventoryResponse response = inventoryService.adjustStock(request, 1L);

        assertEquals(65, response.getActualStock());
        assertEquals(55, response.getAvailableStock());
        verify(adjustmentRepository).save(any(InventoryAdjustmentEntity.class));
        verify(inventoryRepository).save(inventory);
    }
}
