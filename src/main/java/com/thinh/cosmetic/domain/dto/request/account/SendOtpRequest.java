package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendOtpRequest {
    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    private String emailOrPhone;
}
