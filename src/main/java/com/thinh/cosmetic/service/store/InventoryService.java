package com.thinh.cosmetic.service.store;

import com.thinh.cosmetic.domain.dto.request.store.InventoryAdjustmentRequest;
import com.thinh.cosmetic.domain.dto.response.store.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getByStore(Long storeId);
    InventoryResponse getByStoreAndSku(Long storeId, Long skuId) throws Exception;
    InventoryResponse adjustStock(InventoryAdjustmentRequest request, Long employeeId) throws Exception;
    List<InventoryResponse> getLowStockItems(Long storeId);
    Integer getAvailableStock(Long storeId, Long skuId);
}
