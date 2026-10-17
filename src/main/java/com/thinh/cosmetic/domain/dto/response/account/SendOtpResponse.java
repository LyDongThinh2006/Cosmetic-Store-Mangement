package com.thinh.cosmetic.domain.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendOtpResponse {
    private boolean success;
    private String message;
    private String emailOrPhone;
    private String maskedDestination;
    private long expiresInSeconds;
    private String debugOtp;
}
