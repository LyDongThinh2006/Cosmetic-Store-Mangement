package com.thinh.cosmetic.service.purchase;

import com.thinh.cosmetic.domain.dto.request.purchase.PurchaseOrderRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.PurchaseOrderResponse;

import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderResponse create(PurchaseOrderRequest request, Long employeeId) throws Exception;
    PurchaseOrderResponse getById(Long id) throws Exception;
    List<PurchaseOrderResponse> getAll();
    PurchaseOrderResponse confirm(Long id) throws Exception;
}
