package com.thinh.cosmetic.domain.entity.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer_addresses")
public class CustomerAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Customer cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customerEntity;

    @NotBlank(message = "Receiver name cannot be blank")
    @Size(max = 150, message = "Receiver name must not exceed 150 characters")
    @Column(name = "receiver_name", nullable = false, length = 150)
    private String recipientName;

    @NotBlank(message = "Phone cannot be blank")
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "Address detail cannot be blank")
    @Size(max = 255, message = "Address detail must not exceed 255 characters")
    @Column(name = "address_detail", nullable = false, length = 255)
    private String addressDetail;

    @NotBlank(message = "Ward cannot be blank")
    @Size(max = 100, message = "Ward must not exceed 100 characters")
    @Column(name = "ward", nullable = false, length = 100)
    private String ward;

    @Size(max = 100, message = "District must not exceed 100 characters")
    @Column(name = "district", length = 100)
    private String district;

    @NotBlank(message = "Province cannot be blank")
    @Size(max = 100, message = "Province must not exceed 100 characters")
    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @NotNull(message = "IsDefault flag cannot be null")
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;
}
