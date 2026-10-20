package com.thinh.cosmetic.domain.dto.request.promotion;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.VoucherType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherRequest {

    @NotBlank(message = "Mã voucher không được để trống")
    @Size(max = 50, message = "Mã voucher không được vượt quá 50 ký tự")
    private String code;

    @Size(max = 255, message = "Tên chương trình không được vượt quá 255 ký tự")
    private String name;

    private String description;

    @NotNull(message = "Loại giảm giá không được để trống")
    private VoucherType discountType;

    @NotNull(message = "Giá trị giảm không được để trống")
    @DecimalMin(value = "0.0", message = "Giá trị giảm phải lớn hơn hoặc bằng 0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Mức giảm tối đa phải lớn hơn hoặc bằng 0")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Giá trị đơn tối thiểu không được để trống")
    @DecimalMin(value = "0.0", message = "Giá trị đơn tối thiểu phải lớn hơn hoặc bằng 0")
    @Builder.Default
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @NotNull(message = "Ngày bắt đầu hiệu lực không được để trống")
    private LocalDateTime validFrom;

    @NotNull(message = "Ngày kết thúc hiệu lực không được để trống")
    private LocalDateTime validTo;

    @Builder.Default
    private Integer usageLimit = 1000;

    @Builder.Default
    private ActiveStatus status = ActiveStatus.ACTIVE;
}
