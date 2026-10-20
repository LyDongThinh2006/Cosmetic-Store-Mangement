package com.thinh.cosmetic.domain.entity.sales;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import com.thinh.cosmetic.domain.enums.VoucherType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vouchers")
public class VoucherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã voucher không được để trống")
    @Size(max = 50, message = "Mã voucher tối đa 50 ký tự")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Size(max = 255, message = "Tên chương trình tối đa 255 ký tự")
    @Column(name = "name", length = 255)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull(message = "Loại giảm không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private VoucherType discountType;

    @NotNull(message = "Giá trị giảm không được để trống")
    @DecimalMin(value = "0.0", message = "Giá trị giảm phải lớn hơn hoặc bằng 0")
    @Column(name = "discount_value", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Mức giảm tối đa phải lớn hơn hoặc bằng 0")
    @Column(name = "max_discount_amount", precision = 18, scale = 2)
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Giá trị đơn tối thiểu không được để trống")
    @DecimalMin(value = "0.0", message = "Giá trị đơn tối thiểu phải lớn hơn hoặc bằng 0")
    @Column(name = "min_order_amount", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo;

    @Column(name = "usage_limit", nullable = false)
    @Builder.Default
    private Integer usageLimit = 1000;

    @Column(name = "used_count", nullable = false)
    @Builder.Default
    private Integer usedCount = 0;

    @NotNull(message = "Trạng thái không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ActiveStatus status = ActiveStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return (validTo != null && now.isAfter(validTo)) || (validFrom != null && now.isBefore(validFrom));
    }

    public boolean isLimitReached() {
        return usageLimit != null && usedCount != null && usedCount >= usageLimit;
    }

    public boolean isCurrentlyActive() {
        return status == ActiveStatus.ACTIVE && !isExpired() && !isLimitReached();
    }
}
