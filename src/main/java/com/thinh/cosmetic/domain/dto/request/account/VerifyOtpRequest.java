package com.thinh.cosmetic.domain.dto.request.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest {
    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    private String emailOrPhone;

    @NotBlank(message = "Mã OTP không được để trống")
    @Pattern(regexp = "^\\d{6}$", message = "Mã OTP phải bao gồm đúng 6 chữ số")
    private String otp;
}
