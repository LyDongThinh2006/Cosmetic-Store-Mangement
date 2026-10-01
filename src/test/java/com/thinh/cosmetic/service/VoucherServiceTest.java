package com.thinh.cosmetic.service;

import com.thinh.cosmetic.domain.entity.order.VoucherEntity;
import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.DiscountType;
import com.thinh.cosmetic.mapper.order.VoucherMapper;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherServiceTest {

    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private VoucherMapper voucherMapper;

    @InjectMocks
    private VoucherServiceImpl voucherService;

    private VoucherEntity percentVoucher;
    private VoucherEntity fixedVoucher;

    @BeforeEach
    void setUp() {
        percentVoucher = VoucherEntity.builder()
                .id(1L)
                .code("SALE20")
                .discountType(DiscountType.PERCENT)
                .discountValue(new BigDecimal("20"))
                .maxDiscount(new BigDecimal("50000"))
                .minOrderValue(new BigDecimal("100000"))
                .totalQuantity(100)
                .usedQuantity(10)
                .status(ActiveStatus.ACTIVE)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();

        fixedVoucher = VoucherEntity.builder()
                .id(2L)
                .code("GIAM30K")
                .discountType(DiscountType.FIXED)
                .discountValue(new BigDecimal("30000"))
                .minOrderValue(new BigDecimal("200000"))
                .totalQuantity(50)
                .usedQuantity(5)
                .status(ActiveStatus.ACTIVE)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .build();
    }

    @Test
    @DisplayName("Calculate percent discount with cap")
    void testCalculateDiscountPercentWithCap() throws Exception {
        when(voucherRepository.findByCode("SALE20")).thenReturn(Optional.of(percentVoucher));

        // 20% of 400,000 = 80,000, capped at maxDiscount 50,000
        BigDecimal discount = voucherService.calculateDiscount("SALE20", new BigDecimal("400000"));
        assertEquals(new BigDecimal("50000"), discount);
    }

    @Test
    @DisplayName("Calculate percent discount without reaching cap")
    void testCalculateDiscountPercentUnderCap() throws Exception {
        when(voucherRepository.findByCode("SALE20")).thenReturn(Optional.of(percentVoucher));

        // 20% of 150,000 = 30,000, which is below 50,000
        BigDecimal discount = voucherService.calculateDiscount("SALE20", new BigDecimal("150000"));
        assertEquals(0, new BigDecimal("30000").compareTo(discount));
    }

    @Test
    @DisplayName("Calculate fixed discount")
    void testCalculateDiscountFixed() throws Exception {
        when(voucherRepository.findByCode("GIAM30K")).thenReturn(Optional.of(fixedVoucher));

        BigDecimal discount = voucherService.calculateDiscount("GIAM30K", new BigDecimal("250000"));
        assertEquals(new BigDecimal("30000"), discount);
    }

    @Test
    @DisplayName("Reject discount when order total is below minimum")
    void testCalculateDiscountBelowMinimum() {
        when(voucherRepository.findByCode("GIAM30K")).thenReturn(Optional.of(fixedVoucher));

        assertThrows(Exception.class, () ->
                voucherService.calculateDiscount("GIAM30K", new BigDecimal("100000")));
    }

    @Test
    @DisplayName("Reject expired voucher")
    void testCalculateDiscountExpired() {
        percentVoucher.setEndDate(LocalDateTime.now().minusDays(1));
        when(voucherRepository.findByCode("SALE20")).thenReturn(Optional.of(percentVoucher));

        assertThrows(Exception.class, () ->
                voucherService.calculateDiscount("SALE20", new BigDecimal("300000")));
    }
}
