package com.thinh.cosmetic.service.store;

import com.thinh.cosmetic.domain.dto.request.store.StockTransferRequest;
import com.thinh.cosmetic.domain.dto.response.store.StockTransferResponse;

import java.util.List;

public interface StockTransferService {
    StockTransferResponse create(StockTransferRequest request, Long employeeId) throws Exception;
    StockTransferResponse confirmShipment(Long id) throws Exception;
    StockTransferResponse confirmReceipt(Long id) throws Exception;
    StockTransferResponse getById(Long id) throws Exception;
    List<StockTransferResponse> getAll();
}
