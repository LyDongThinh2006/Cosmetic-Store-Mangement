package com.thinh.cosmetic.domain.entity.inventory;

import com.thinh.cosmetic.domain.enums.ActiveStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stores")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store name cannot be blank")
    @Size(max = 150, message = "Store name must not exceed 150 characters")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotBlank(message = "Province cannot be blank")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ActiveStatus status = ActiveStatus.ACTIVE;
}
