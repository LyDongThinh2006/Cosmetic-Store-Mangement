package com.thinh.cosmetic.service.order;

import com.thinh.cosmetic.domain.dto.request.promotion.VoucherRequest;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherResponse;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherValidationResponse;
import com.thinh.cosmetic.domain.entity.sales.VoucherEntity;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

    VoucherResponse createVoucher(VoucherRequest request) throws Exception;

    VoucherResponse updateVoucher(Long id, VoucherRequest request) throws Exception;

    VoucherResponse toggleStatus(Long id) throws Exception;

    VoucherResponse getById(Long id) throws Exception;

    VoucherResponse getByCode(String code) throws Exception;

    List<VoucherResponse> getAllVouchers();

    List<VoucherResponse> getActiveVouchers();

    VoucherValidationResponse validateVoucher(String code, BigDecimal subtotal, BigDecimal shippingFee);

    VoucherEntity validateAndApply(String code, BigDecimal subtotal) throws Exception;

    BigDecimal calculateDiscount(VoucherEntity voucher, BigDecimal subtotal);

    BigDecimal calculateShippingDiscount(VoucherEntity voucher, BigDecimal shippingFee);

    void incrementUsage(VoucherEntity voucher);

    void releaseUsage(VoucherEntity voucher);
}
