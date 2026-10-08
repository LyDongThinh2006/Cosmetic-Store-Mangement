package com.thinh.cosmetic.domain.dto.request.order;

import com.thinh.cosmetic.domain.enums.DeliveryMethod;
import com.thinh.cosmetic.domain.enums.PaymentMethod;
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
public class OrderRequest {
    private Long customerAddressId;

    private Long fulfillmentStoreId;

    @Size(max = 150, message = "Recipient name must not exceed 150 characters")
    private String recipientName;

    @Size(max = 20, message = "Recipient phone must not exceed 20 characters")
    private String recipientPhone;

    @Size(max = 500, message = "Shipping address must not exceed 500 characters")
    private String shippingAddress;

    @Size(max = 100, message = "Ward must not exceed 100 characters")
    private String ward;

    @Size(max = 100, message = "District must not exceed 100 characters")
    private String district;

    @Size(max = 100, message = "Province must not exceed 100 characters")
    private String province;

    private DeliveryMethod deliveryMethod;

    @NotNull(message = "Payment method cannot be null")
    private PaymentMethod paymentMethod;

    @Size(max = 500, message = "Note must not exceed 500 characters")
    private String note;

    private String voucherCode;
}
