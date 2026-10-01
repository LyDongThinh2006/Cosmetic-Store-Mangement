package com.thinh.cosmetic.service.order;

import com.thinh.cosmetic.domain.dto.request.order.VoucherRequest;
import com.thinh.cosmetic.domain.dto.response.order.VoucherResponse;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {
    VoucherResponse create(VoucherRequest request) throws Exception;
    VoucherResponse getById(Long id) throws Exception;
    VoucherResponse getByCode(String code) throws Exception;
    List<VoucherResponse> getAll();
    VoucherResponse update(Long id, VoucherRequest request) throws Exception;
    void deactivate(Long id) throws Exception;
    BigDecimal calculateDiscount(String code, BigDecimal orderTotal) throws Exception;
}
