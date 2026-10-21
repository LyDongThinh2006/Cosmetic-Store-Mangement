package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.dto.request.promotion.VoucherRequest;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherResponse;
import com.thinh.cosmetic.domain.dto.response.promotion.VoucherValidationResponse;
import com.thinh.cosmetic.domain.entity.sales.VoucherEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.VoucherType;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.repository.order.VoucherRepository;
import com.thinh.cosmetic.service.order.impl.VoucherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    private VoucherEntity sampleVoucher;

    @BeforeEach
    void setUp() {
        sampleVoucher = VoucherEntity.builder()
                .id(1L)
                .code("LUNEA10")
                .name("Giảm 10% Tối Đa 100K")
                .discountType(VoucherType.PERCENT)
                .discountValue(new BigDecimal("10.00"))
                .maxDiscountAmount(new BigDecimal("100000.00"))
                .minOrderAmount(new BigDecimal("200000.00"))
                .usageLimit(100)
                .usedCount(10)
                .validFrom(LocalDateTime.now().minusDays(1))
                .validTo(LocalDateTime.now().plusDays(30))
                .status(ActiveStatus.ACTIVE)
                .build();
    }

    @Test
    @DisplayName("Tạo voucher mới thành công khi mã chưa tồn tại")
    void testCreateVoucherSuccess() throws Exception {
        VoucherRequest request = VoucherRequest.builder()
                .code("NEWYEAR2026")
                .name("Ưu đãi năm mới")
                .discountType(VoucherType.FIXED)
                .discountValue(new BigDecimal("50000.00"))
                .minOrderAmount(new BigDecimal("300000.00"))
                .usageLimit(500)
                .status(ActiveStatus.ACTIVE)
                .build();

        when(voucherRepository.existsByCodeIgnoreCase("NEWYEAR2026")).thenReturn(false);
        when(voucherRepository.save(any(VoucherEntity.class))).thenAnswer(invocation -> {
            VoucherEntity entity = invocation.getArgument(0);
            entity.setId(2L);
            return entity;
        });

        VoucherResponse response = voucherService.createVoucher(request);

        assertNotNull(response);
        assertEquals("NEWYEAR2026", response.getCode());
        verify(voucherRepository, times(1)).save(any(VoucherEntity.class));
    }

    @Test
    @DisplayName("Tạo voucher trùng mã phải ném DUPLICATE_VOUCHER_CODE")
    void testCreateVoucherDuplicateCodeThrowsException() {
        VoucherRequest request = VoucherRequest.builder()
                .code("LUNEA10")
                .name("Duplicate code")
                .discountType(VoucherType.PERCENT)
                .discountValue(new BigDecimal("10.00"))
                .build();

        when(voucherRepository.existsByCodeIgnoreCase("LUNEA10")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> voucherService.createVoucher(request));
        assertEquals(ErrorCode.DUPLICATE_VOUCHER_CODE, ex.getErrorCode());
        verify(voucherRepository, never()).save(any());
    }

    @Test
    @DisplayName("Kiểm tra voucher PERCENT có áp trần maxDiscountAmount")
    void testValidateVoucherPercentWithCap() {
        when(voucherRepository.findByCodeIgnoreCase("LUNEA10")).thenReturn(Optional.of(sampleVoucher));

        // Subtotal = 2,000,000đ -> 10% = 200,000đ nhưng trần là 100,000đ
        VoucherValidationResponse res = voucherService.validateVoucher("LUNEA10", new BigDecimal("2000000.00"), new BigDecimal("30000.00"));

        assertTrue(res.isValid());
        assertEquals(0, new BigDecimal("100000.00").compareTo(res.getDiscountAmount()));
        assertEquals(0, new BigDecimal("1900000.00").compareTo(res.getFinalSubtotal()));
    }

    @Test
    @DisplayName("Kiểm tra voucher FIXED trừ đúng số tiền cố định")
    void testValidateVoucherFixed() {
        sampleVoucher.setDiscountType(VoucherType.FIXED);
        sampleVoucher.setDiscountValue(new BigDecimal("50000.00"));
        sampleVoucher.setMaxDiscountAmount(null);

        when(voucherRepository.findByCodeIgnoreCase("GIAM50K")).thenReturn(Optional.of(sampleVoucher));

        VoucherValidationResponse res = voucherService.validateVoucher("GIAM50K", new BigDecimal("500000.00"), new BigDecimal("30000.00"));

        assertTrue(res.isValid());
        assertEquals(0, new BigDecimal("50000.00").compareTo(res.getDiscountAmount()));
        assertEquals(0, new BigDecimal("450000.00").compareTo(res.getFinalSubtotal()));
    }

    @Test
    @DisplayName("Kiểm tra voucher khi đơn hàng chưa đạt giá trị tối thiểu")
    void testValidateVoucherMinOrderNotMet() {
        when(voucherRepository.findByCodeIgnoreCase("LUNEA10")).thenReturn(Optional.of(sampleVoucher));

        // Min order là 200,000đ nhưng subtotal là 150,000đ
        VoucherValidationResponse res = voucherService.validateVoucher("LUNEA10", new BigDecimal("150000.00"), new BigDecimal("30000.00"));

        assertFalse(res.isValid());
        assertTrue(res.getMessage().contains("chưa đạt giá trị tối thiểu"));
    }

    @Test
    @DisplayName("Kiểm tra voucher đã hết hạn")
    void testValidateVoucherExpired() {
        sampleVoucher.setValidTo(LocalDateTime.now().minusDays(1));
        when(voucherRepository.findByCodeIgnoreCase("LUNEA10")).thenReturn(Optional.of(sampleVoucher));

        VoucherValidationResponse res = voucherService.validateVoucher("LUNEA10", new BigDecimal("300000.00"), new BigDecimal("30000.00"));

        assertFalse(res.isValid());
        assertTrue(res.getMessage().contains("đã hết hạn"));
    }

    @Test
    @DisplayName("Kiểm tra voucher đạt giới hạn số lượt sử dụng")
    void testValidateVoucherLimitReached() {
        sampleVoucher.setUsageLimit(10);
        sampleVoucher.setUsedCount(10);
        when(voucherRepository.findByCodeIgnoreCase("LUNEA10")).thenReturn(Optional.of(sampleVoucher));

        VoucherValidationResponse res = voucherService.validateVoucher("LUNEA10", new BigDecimal("300000.00"), new BigDecimal("30000.00"));

        assertFalse(res.isValid());
        assertTrue(res.getMessage().contains("hết số lượt sử dụng"));
    }

    @Test
    @DisplayName("Tăng lượt sử dụng voucher khi đặt hàng thành công")
    void testIncrementUsage() {
        sampleVoucher.setUsedCount(5);

        voucherService.incrementUsage(sampleVoucher);

        assertEquals(6, sampleVoucher.getUsedCount());
        verify(voucherRepository, times(1)).save(sampleVoucher);
    }

    @Test
    @DisplayName("Hoàn lại lượt sử dụng voucher khi hủy đơn hàng")
    void testReleaseUsage() {
        sampleVoucher.setUsedCount(5);

        voucherService.releaseUsage(sampleVoucher);

        assertEquals(4, sampleVoucher.getUsedCount());
        verify(voucherRepository, times(1)).save(sampleVoucher);
    }
}
