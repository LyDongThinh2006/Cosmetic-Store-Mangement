package com.thinh.cosmetic.service.purchase;

import com.thinh.cosmetic.domain.dto.request.purchase.SupplierRequest;
import com.thinh.cosmetic.domain.dto.response.purchase.SupplierResponse;

import java.util.List;

public interface SupplierService {
    SupplierResponse create(SupplierRequest request);
    SupplierResponse getById(Long id) throws Exception;
    List<SupplierResponse> getAll();
    SupplierResponse update(Long id, SupplierRequest request) throws Exception;
    void deactivate(Long id) throws Exception;
}
